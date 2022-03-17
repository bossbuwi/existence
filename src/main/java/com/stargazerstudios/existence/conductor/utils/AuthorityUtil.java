package com.stargazerstudios.existence.conductor.utils;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.constants.EnumRank;
import com.stargazerstudios.existence.conductor.constants.EnumUtilNumberOutput;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.repository.RoleDAO;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthorityUtil {

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private StringUtil stringUtil;

    public boolean checkAuthority(String role) {
        if (isBanned()) return false;

        String roleName = stringUtil.checkInputTrimToUpper(role);
        if (roleName.equals(EnumUtilOutput.EMPTY.getValue()) || roleName.equals(EnumUtilOutput.INVALID.getValue()))
            return false;

        long highestRank = getHighestRank();
        long inputRank;
        try {
            inputRank = EnumRank.valueOf(role).getValue();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

        return highestRank <= inputRank;
    }

    public String getAuthUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public long getHighestRank() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> userData = userDAO.findByUsername(authentication.getName());
        if (userData.isEmpty()) return EnumUtilNumberOutput.INVALID.getValue();

        User user = userData.get();
        List<Role> roles = new ArrayList<>(user.getRoles());
        if (roles.isEmpty()) return EnumUtilNumberOutput.INVALID.getValue();

        roles.sort(Comparator.comparing(Role::getRank));
        return roles.get(0).getRank();
    }

    public long getHighestRank(User user) {
        if (user == null) return EnumUtilNumberOutput.INVALID.getValue();

        List<Role> roles = new ArrayList<>(user.getRoles());
        if (roles.isEmpty()) return EnumUtilNumberOutput.INVALID.getValue();

        roles.sort(Comparator.comparing(Role::getRank));
        return roles.get(0).getRank();
    }

    public long getHighestRank(List<Role> roles) {
        if (roles.isEmpty()) return EnumUtilNumberOutput.INVALID.getValue();

        roles.sort(Comparator.comparing(Role::getRank));
        return roles.get(0).getRank();
    }

    public boolean hasHigherRank(long checkRank, long compareRank) {
        return checkRank <= compareRank;
    }

    public boolean isBanned() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
        GrantedAuthority authority = authorities.stream()
                .filter(grantedAuthority ->
                        EnumAuthorization.BANNED.getValue().equals(grantedAuthority.getAuthority()))
                .findAny()
                .orElse(null);
        return authority != null;
    }

    public boolean isBanned(Set<Role> roles) {
        List<Role> roleList = new ArrayList<>(roles);
        Role role = roleList.stream()
                .filter(banned ->
                    EnumAuthorization.BANNED.getValue().equals(banned.getName())
                )
                .findAny()
                .orElse(null);
        return role != null;
    }

    public boolean isBanned(List<GrantedAuthority> authorities) {
        GrantedAuthority authority = authorities.stream()
                .filter(grantedAuthority ->
                        EnumAuthorization.BANNED.getValue().equals(grantedAuthority.getAuthority()))
                .findAny()
                .orElse(null);
        return authority != null;
    }
}
