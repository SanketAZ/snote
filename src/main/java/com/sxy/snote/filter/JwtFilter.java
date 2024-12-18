//package com.sxy.snote.filter;
//
//
//import com.sxy.snote.exception.AuthException;
//import com.sxy.snote.service.impl.JWTService;
//import com.sxy.snote.service.impl.MyUserDetailsService;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.security.SignatureException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.ApplicationContext;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Service;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//
//import java.io.IOException;
//
//
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JWTService jwtService;
//
//    @Autowired
//    private ApplicationContext context;
//
//    //
//    @Autowired
//    @Qualifier("handlerExceptionResolver")
//    private HandlerExceptionResolver exceptionResolver;
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
//                UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(userName);
//                if (jwtService.validateToken(token, userDetails)) {
//                    UsernamePasswordAuthenticationToken authToken =
//                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                } else {
//                    System.out.println(1);
//                    throw new AuthException("Invalid JWT token");
//                }
//            }
//            else {
//                System.out.println(2);
//                throw new AuthException("Invalid JWT token");
//            }
//        }catch (Exception ex)
//        {
//            AuthException authException=new AuthException(ex.getMessage());
//            exceptionResolver.resolveException(request,response,null,authException);
//            return;
//        }
//
//        filterChain.doFilter(request,response);
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        String uri =request.getRequestURI();
//        //return uri.startsWith("/sign-up") || uri.startsWith("/sign-in") || uri.startsWith("/refresh-token");
//        return true;
//    }
//
//}
