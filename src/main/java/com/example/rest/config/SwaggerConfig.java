package com.example.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
//                API의 정보
                .apiInfo(apiInfo())
//                기본 응답 값 사용하지 않는다.
                .useDefaultResponseMessages(false)
                .select()
//                Swagger가 RestController를 스캔할 부분
                .apis(RequestHandlerSelectors.basePackage("com.example.rest"))
//                해당 하위 경로를 가지는 API에 대해 문서를 생성한다.
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 제목
                .title("Rest Api Docs")
                // 설명
                .description("description this api")
//                버전
                .version("1.0.0")
                .build();
    }

}
