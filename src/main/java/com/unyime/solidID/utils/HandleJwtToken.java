package com.unyime.solidID.utils;

import com.unyime.solidID.services.impl.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HandleJwtToken {

    private  JwtServiceImpl jwtServiceImpl;

    @Autowired
    public HandleJwtToken(JwtServiceImpl jwtServiceImpl) {
        this.jwtServiceImpl = jwtServiceImpl;
    }

    public HandleJwtToken(){}

    public Boolean verifyToken(String reqHeader, String orgEmail){
        final String jwt;
        final String userEmail;

        if (reqHeader != null && reqHeader.startsWith("Bearer ")){
            jwt = reqHeader.substring(7);
            userEmail = jwtServiceImpl.extractUsername(jwt);
            return userEmail.equals(orgEmail);
        }
        return false;
    }

}

// This comment is to trigger deployement on render.com
