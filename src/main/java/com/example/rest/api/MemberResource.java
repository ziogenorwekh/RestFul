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
@RequestMapping("/api")
public class MemberResource {
    private final MemberService memberService;

    @PostMapping("/members")
    @ApiOperation(value = "회원가입", notes = "Vo 폼에 맞는 정보를 갖고 회원 가입한다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "가입 성공"),
            @ApiResponse(code = 400, message = "올바르지 않은 형식")
    })
    public ResponseEntity<Long> create(@RequestBody @Validated MemberReqVo memberReqVo) {
        Long save = memberService.save(memberReqVo);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(save).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/members/{id}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공"),
            @ApiResponse(code = 404, message = "조회 실패")
    })
    public ResponseEntity<MappingJacksonValue> retrieveUsers(@PathVariable Long id) {
        Member member = memberService.findOne(id);
        MemberRespVo respVo = new ModelMapper().map(member, MemberRespVo.class);


//        HATEOS 사용
        EntityModel<MemberRespVo> entityModel = EntityModel.of(respVo);
//        해당 자원에 대해 객체를 생성하고, 해당 자원의 메서드를 참조한다.
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).update(id,null));

//        참조 설정
        entityModel.add(linkBuilder.withRel("update-member"));
        entityModel.add(linkBuilder.withRel("delete-member"));

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("memberId", "email", "name");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("MemberInfo", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(respVo);
        jacksonValue.setFilters(filterProvider);
        return ResponseEntity.ok(jacksonValue);
    }


    @GetMapping("/members")
    @ApiResponses( {
            @ApiResponse(code = 200, message = "조회 성공")
    })
    public ResponseEntity<List<Member>> retrieveAllUsers() {
        List<Member> members = memberService.findAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @PutMapping("/members/{id}")
    @ApiResponses({
            @ApiResponse(code = 202, message = "수정 성공"),
            @ApiResponse(code = 400, message = "올바르지 않은 형식"),
            @ApiResponse(code = 404, message = "수정 실패")
    })
    public ResponseEntity<MappingJacksonValue> update(@PathVariable Long id, @RequestBody @Validated MemberReqVo memberReqVo) {
        Member member = memberService.update(id, memberReqVo);
        MemberRespVo respVo = new ModelMapper().map(member, MemberRespVo.class);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("memberId", "email", "name");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("MemberInfo", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(respVo);
        jacksonValue.setFilters(filterProvider);

        return ResponseEntity.accepted().body(jacksonValue);
    }

    @DeleteMapping("/members/{id}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "삭제 성공"),
            @ApiResponse(code = 404, message = "삭제 실패")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.ok().build();
    }
}
