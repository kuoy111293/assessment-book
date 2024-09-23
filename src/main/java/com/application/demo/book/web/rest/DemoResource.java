package com.application.demo.book.web.rest;

import com.application.demo.book.security.AuthoritiesConstants;
import com.application.demo.book.web.rest.base.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.InetSocketAddress;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoResource {

    private final ObjectMapper mapper;

    public DemoResource(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping("/auth-info")
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    public ResponseEntity<?> viewUserAuth(@RequestHeader HttpHeaders headers) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        InetSocketAddress host = headers.getHost();
        String url = "http://" + host.getHostName() + ":" + host.getPort();
        System.out.println("=====>: " + url);

        headers.forEach((key, value) -> {
            System.out.println("--->" + key + ": " + value);
        });

        return new ResponseEntity<>(new BaseResponse<>(true, "Inquiry successfully.", auth), HttpStatus.OK);
    }
}
