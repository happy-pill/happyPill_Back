package com.happypill.application.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi(OpenApiCustomizer globalResponseCustomizer,
                                    OperationCustomizer securityResponseCustomizer) {
        return GroupedOpenApi.builder()
                .group("모든 API")
                .pathsToMatch("/api/**")
                .addOpenApiCustomizer(globalResponseCustomizer)
                .addOperationCustomizer(securityResponseCustomizer)
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi(OpenApiCustomizer globalResponseCustomizer,
                                   OperationCustomizer securityResponseCustomizer) {
        return GroupedOpenApi.builder()
                .group("관리자 API")
                .pathsToMatch("/api/admin/**")
                .addOpenApiCustomizer(globalResponseCustomizer)
                .addOperationCustomizer(securityResponseCustomizer)
                .build();
    }

    @Bean
    public GroupedOpenApi userApi(OpenApiCustomizer globalResponseCustomizer,
                                  OperationCustomizer securityResponseCustomizer) {
        return GroupedOpenApi.builder()
                .group("유저 API")
                .pathsToMatch("/api/user/**")
                .addOpenApiCustomizer(globalResponseCustomizer)
                .addOperationCustomizer(securityResponseCustomizer)
                .build();
    }

    @Bean
    public GroupedOpenApi productApi(OpenApiCustomizer globalResponseCustomizer,
                                     OperationCustomizer securityResponseCustomizer) {
        return GroupedOpenApi.builder()
                .group("상품 API")
                .pathsToMatch("/api/products/**")
                .addOpenApiCustomizer(globalResponseCustomizer)
                .addOperationCustomizer(securityResponseCustomizer)
                .build();
    }

    @Bean
    public GroupedOpenApi categoryApi(OpenApiCustomizer globalResponseCustomizer,
                                      OperationCustomizer securityResponseCustomizer) {
        return GroupedOpenApi.builder()
                .group("카테고리 API")
                .pathsToMatch("/api/categories/**")
                .addOpenApiCustomizer(globalResponseCustomizer)
                .addOperationCustomizer(securityResponseCustomizer)
                .build();
    }

    @Bean
    public GroupedOpenApi orderApi(OpenApiCustomizer globalResponseCustomizer,
                                   OperationCustomizer securityResponseCustomizer) {
        return GroupedOpenApi.builder()
                .group("주문 API")
                .pathsToMatch("/api/order/**")
                .addOpenApiCustomizer(globalResponseCustomizer)
                .addOperationCustomizer(securityResponseCustomizer)
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Happypill API")
                        .description("해피필 API 명세서"));
    }

    /**
     * @PreAuthorize 붙은 API 는 자동으로 401, 403 응답을 문서화한다
     */
    @Bean
    public OperationCustomizer securityResponseCustomizer() {
        return (operation, handlerMethod) -> {
            PreAuthorize preAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
            if (preAuthorize != null) {
                operation.getResponses().addApiResponse("401", new ApiResponse().description("인증 실패 (json 에 message 만 포함)"));
                operation.getResponses().addApiResponse("403", new ApiResponse().description("권한 없음 (json 에 message 만 포함)"));
            }
            return operation;
        };
    }

    @Bean
    public OpenApiCustomizer globalResponseCustomizer() {
        return openApi -> {

            Schema<?> errorResponseSchema = new Schema<>()
                    .type("object")
                    .name("ErrorResponse")
                    .addProperty("message", new StringSchema().description("에러 메시지"))
                    .addProperty("data", new Schema<>().type("object").description("추가 컨텍스트 입력 필드"));

            if (openApi.getComponents() == null) {
                openApi.setComponents(new Components());
            }
            openApi.getComponents().addSchemas("ErrorResponse", errorResponseSchema); //ErrorResponse.class 가 Schema 로 등록되지 않는 문제로 인해 직접 등록

            openApi.getPaths().forEach((path, pathItem) -> {
                pathItem.readOperationsMap().forEach((httpMethod, operation) -> {

                    ApiResponses responses = operation.getResponses();

                    if (httpMethod == PathItem.HttpMethod.POST || httpMethod == PathItem.HttpMethod.PATCH || httpMethod == PathItem.HttpMethod.PUT) {
                        addIfNotPresent(responses, "400", "요청 DTO 의 유효성 검증에 실패했을 때", "ErrorResponse");
                    }
                    if ((path.contains("{") && path.contains("}")) || httpMethod == PathItem.HttpMethod.GET) { //경로변수나 쿼리파라미터로 요청한 객체가 없는 경우
                        addIfNotPresent(responses, "404", "요청한 객체가 존재하지 않을 때", "ErrorResponse");
                    }

                    if (path.startsWith("/api")) {
                        addIfNotPresent(responses, "500", "서버 내부 오류", "ErrorResponse");
                    }
                });
            });
        };
    }

    //ApiResponses 에 중복된 상태코드 응답이 정의되지 않도록 방지하기 위한 메서드(같은 상태코드를 두 번 추가하면 기존 응답을 덮어쓰는 문제로 인해)
    private void addIfNotPresent(ApiResponses responses, String code, String description, String schemaName) {
        if (!responses.containsKey(code)) {
            ApiResponse response = new ApiResponse().description(description);

            if (schemaName != null) {
                Content content = new Content();
                MediaType mediaType = new MediaType();
                Schema<?> schema = new Schema<>().$ref("#/components/schemas/" + schemaName);
                mediaType.schema(schema);
                content.addMediaType("application/json", mediaType);
                response.content(content);
            }

            responses.addApiResponse(code, response);
        }
    }
}