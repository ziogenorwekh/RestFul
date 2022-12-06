package com.example.rest.api;

import com.example.rest.domain.Member;
import com.example.rest.service.MemberService;
import com.example.rest.vo.MemberReqVo;
import com.example.rest.vo.MemberRespVo;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminMemberResource {
    private final MemberService memberService;

    @GetMapping("/members/{id}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공"),
            @ApiResponse(code = 404, message = "조회 실패")
    })
    public ResponseEntity<MappingJacksonValue> retrieveUsers(@PathVariable Long id) {
        Member member = memberService.findOne(id);
        MemberRespVo respVo = new ModelMapper().map(member, MemberRespVo.class);

        // 지정된 필드만 JSON 반환, 나머지는 무시
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "memberId", "email", "name");

        // JsonFilter에서 MemberInfo의 이름을 가진 Vo 객체에 필터 설정
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("MemberInfo", filter);

        // 필터가 적용되도록 다른 타입으로 변환한다.
        MappingJacksonValue jacksonValue = new MappingJacksonValue(respVo);
        // 적용된 필터 설정
        jacksonValue.setFilters(filterProvider);


        return ResponseEntity.ok(jacksonValue);
    }
}
