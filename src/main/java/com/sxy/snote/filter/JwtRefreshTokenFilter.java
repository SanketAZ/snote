//package com.sxy.snote.filter;
//
//import com.sxy.snote.exception.AuthException;
//import com.sxy.snote.repository.RefreshTokenRepo;
//import com.sxy.snote.service.impl.JWTService;
//import com.sxy.snote.service.impl.MyUserDetailsService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.ApplicationContext;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Service;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//
//import java.io.IOException;
//
//
//public class JwtRefreshTokenFilter extends OncePerRequestFilter {
////    @Autowired
////    private  JWTService jwtService;
////
////    @Autowired
////    private  ApplicationContext context;
////
////    @Autowired
////    private  RefreshTokenRepo refreshTokenRepo;
////
////    //
////    @Autowired
////    @Qualifier("handlerExceptionResolver")
////    private HandlerExceptionResolver exceptionResolver;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader=request.getHeader("Authorization");
//        String token=null;
//        String userName=null;
//
//        try {
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                token = authHeader.substring(7);
//                userName = jwtService.extractUserName(token);
//            }
//
//            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                //Check if refreshToken isPresent in database and is valid
//                var isRefreshTokenValidInDatabase=refreshTokenRepo.findByRefreshToken(jwtService.extractTokenValue(token))
//                        .map(refreshTokenEntity -> !refreshTokenEntity.isRevoked())
//                        .orElse(false);
//
//                UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(userName);
//                if (jwtService.validateToken(token, userDetails) && isRefreshTokenValidInDatabase) {
//                    UsernamePasswordAuthenticationToken authToken =
//                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                } else {
//                    System.out.println(1);
//                    throw new AuthException("Invalid JWT token");
//                }
//
//            } else {
//                throw new AuthException("Invalid JWT token");
//            }
//        }catch (Exception ex)
//        {
//            AuthException authException=new AuthException(ex.getMessage());
//            exceptionResolver.resolveException(request,response,null,authException);
//        }
//
//        filterChain.doFilter(request,response);
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        String uri =request.getRequestURI();
//        //return !uri.startsWith("/refresh-token") || !uri.startsWith("/logout");
//        return true;
//    }
//
//}
