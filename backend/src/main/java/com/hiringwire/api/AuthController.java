    package com.hiringwire.api;
    
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.web.bind.annotation.CrossOrigin;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    
    import com.hiringwire.exception.HiringWireException;
    import com.hiringwire.jwt.AuthenticationRequest;
    import com.hiringwire.jwt.AuthenticationResponse;
    import com.hiringwire.jwt.JwtHelper;
    
    @RestController
    @CrossOrigin
    @RequestMapping("/auth")
    @RequiredArgsConstructor
    public class AuthController {
        private final UserDetailsService userDetailsService;
        private final AuthenticationManager authenticationManager;
        private final JwtHelper jwtHelper;
        
        @PostMapping("/login")
        public ResponseEntity<?>createAuthenticationToken(@RequestBody AuthenticationRequest request) throws HiringWireException {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
                );
            } catch (AuthenticationException e) {
                throw new HiringWireException("Incorrect username or password");
            }
    
            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            final String jwt = jwtHelper.generateToken(userDetails);
    
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        }
    }
