package com.sxy.snote.service.impl;


import com.sxy.snote.model.Client;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {

    private final SecretKey secretKey;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JWTService(JwtEncoder jwtEncoder, @Qualifier("jwtDecoder") JwtDecoder jwtDecoder)
    {
        secretKey= Jwts.SIG.HS256.key().build();
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String genrateToken(Client client)
    {
        //Map<String,Object> claims=new HashMap<>();

        String roles=getRolesOfUser(client);
        String permissions=getPermissionsFromRoles(roles);

       // claims.put("scope",permissions);

        JwtClaimsSet claims= JwtClaimsSet.builder()
                .issuer("snote")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(15 , ChronoUnit.MINUTES))
                .subject(client.getUsername())
                .claim("scope", permissions)
                .build();

//        return  Jwts.builder()
//                .claims()
//                .add(claims)
//                .subject(client.getUsername())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis()+30 * 60 * 1000))
//                .and()
//                .signWith(getKey())
//                .compact();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String genrateRefreshToken(Client client)
    {
        JwtClaimsSet claims= JwtClaimsSet.builder()
                .issuer("snote")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(15 , ChronoUnit.DAYS))
                .subject(client.getUsername())
                .claim("scope", "REFRESH-TOKEN")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String extractTokenValue(String headerToken)
    {
        Jwt jwtToken =jwtDecoder.decode(headerToken);
        return jwtToken.getTokenValue();
    }

    private SecretKey getKey() {
        System.out.println(secretKey.hashCode());
        return secretKey;
    }

    public String extractUserName(String token) {
        Jwt jwtToken =jwtDecoder.decode(token);
        return jwtToken.getSubject();
    }

    public String getClaim(String claimName,String token) {
        Jwt jwtToken =jwtDecoder.decode(token);
        return jwtToken.getClaim(claimName);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        final boolean is_Refresh_Token=getClaim("scope",token).contains("REFRESH-TOKEN");
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token) && !is_Refresh_Token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        Jwt jwtToken =jwtDecoder.decode(token);

        //return extractExpiration(token).before(new Date());
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //-------------------------------------------------------------------------------

    private String getRolesOfUser(Client client)
    {
        return client.getRoles().replace(","," ");
    }

    private String getPermissionsFromRoles(String roles)
    {
        Set<String> permissions=new HashSet<>();

        if(roles.contains("ROLE_USER"))
        {
            permissions.addAll(List.of("READ","WRITE","DELETE"));
        }

        return String.join(" ",permissions);
    }

}
