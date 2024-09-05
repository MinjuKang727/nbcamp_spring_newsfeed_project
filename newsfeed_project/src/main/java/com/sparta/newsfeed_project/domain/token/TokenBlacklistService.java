//package com.sparta.newsfeed_project.domain.token;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//@RequiredArgsConstructor
//public class TokenBlacklistService {
//    private final TokenBlacklistRepository tokenBlacklistRepository;
//
//
//    public void addTokenToBlackList(String token) {
//        TokenBlacklistEntry entry = new TokenBlacklistEntry(token, expirationTime);
//        this.tokenBlacklistRepository.save()
//    }
//
//    public boolean isTokenBlackListed(String token) {
//        return this.blacklist.contains(token);
//    }
//
//    public void removeTokenFromBlackList(String token) {
//        this.blacklist.remove(token);
//    }
//}
