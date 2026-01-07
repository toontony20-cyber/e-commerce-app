package com.springbootfirstproject.ecommerceapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if ("google".equals(registrationId)) {
            return processGoogleUser(oauth2User);
        }

        return oauth2User;
    }

    private OAuth2User processGoogleUser(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String googleId = oauth2User.getAttribute("id");
        String imageUrl = oauth2User.getAttribute("picture");

        // Check if user already exists
        User existingUser = userRepository.findByProviderAndProviderId("google", googleId).orElse(null);

        if (existingUser == null) {
            // Check if user exists with same email
            existingUser = userRepository.findByEmail(email).orElse(null);

            if (existingUser == null) {
                // Create new user
                User newUser = new User();
                newUser.setName(name);
                newUser.setEmail(email);
                newUser.setProvider("google");
                newUser.setProviderId(googleId);
                newUser.setImageUrl(imageUrl);
                userRepository.save(newUser);
                System.out.println("Created new Google user: " + email);
            } else {
                // Update existing user with OAuth2 info
                existingUser.setProvider("google");
                existingUser.setProviderId(googleId);
                existingUser.setImageUrl(imageUrl);
                userRepository.save(existingUser);
                System.out.println("Updated existing user with Google login: " + email);
            }
        } else {
            System.out.println("Google user logged in: " + email);
        }

        return oauth2User;
    }
}