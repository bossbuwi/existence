package com.stargazerstudios.existence.conductor.filters;

import com.stargazerstudios.existence.conductor.constants.WebSecurityConstants;
import com.stargazerstudios.existence.conductor.constants.WebSecurityURI;
import com.stargazerstudios.existence.conductor.erratum.universal.BadJsonWebTokenException;
import com.stargazerstudios.existence.conductor.utils.JwtUtil;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Resource(name = "UserServiceImpl")
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String authHeader = request.getHeader(WebSecurityConstants.HEADER_STRING);
        String username = null;
        String authToken = null;
        if (authHeader != null && authHeader.startsWith(WebSecurityConstants.TOKEN_PREFIX)) {
            authToken = authHeader.replace(WebSecurityConstants.TOKEN_PREFIX,"");
            try {
                username = jwtUtil.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                logger.warn("An error occurred while fetching Username from Token", e);
                SecurityContextHolder.clearContext();
                BadJsonWebTokenException ex = new BadJsonWebTokenException();
                resolver.resolveException(request, response, null, ex);
            } catch (ExpiredJwtException e) {
                logger.warn("The token has expired", e);
                SecurityContextHolder.clearContext();
                BadJsonWebTokenException ex = new BadJsonWebTokenException();
                resolver.resolveException(request, response, null, ex);
            } catch(SignatureException e){
                logger.warn("Authentication Failed. Username or Password not valid.");
                SecurityContextHolder.clearContext();
                BadJsonWebTokenException ex = new BadJsonWebTokenException();
                resolver.resolveException(request, response, null, ex);
            }
        } else {
            SecurityContextHolder.clearContext();
            logger.warn("Couldn't find bearer string, header will be ignored");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = jwtUtil.getAuthenticationToken(
                        authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        Collection<String> excludeUrlPatterns = new ArrayList<>();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        excludeUrlPatterns.add(WebSecurityURI.Unguarded.URI_TEST);
        excludeUrlPatterns.add(WebSecurityURI.Unguarded.URI_LOGIN);
        excludeUrlPatterns.add(WebSecurityURI.Unguarded.URI_FRONTEND_ROOT);
        excludeUrlPatterns.add(WebSecurityURI.Unguarded.URI_FRONTEND_PATHS);
        return excludeUrlPatterns.stream().anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }
}
