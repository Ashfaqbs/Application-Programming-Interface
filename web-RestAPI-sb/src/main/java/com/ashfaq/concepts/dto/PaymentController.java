package com.ashfaq.concepts.dto;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
@Validated //need to add @Validated and @Valid at the method argument
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) {
        Payment createdPayment = paymentService.createPayment(payment);
        return ResponseEntity.ok(createdPayment);
    }
}


//JSON 

//{
//    "cost": 0.0,
//    "productName": "",
//    "description": "A very long description that exceeds the 200 characters limit. A very long description that exceeds the 200 characters limit. A very long description that exceeds the 200 characters limit. A very long description that exceeds the 200 characters limit. A very long description that exceeds the 200 characters limit.",
//    "transactionId": "",
//    "result": null
//}


//error
//{
//    "timestamp": "2024-08-03T11:29:36.850+00:00",
//    "status": 400,
//    "error": "Bad Request",
//    "trace": "org.springframework.web.bind.MethodArgumentNotValidException: Validation failed for argument [0] in public org.springframework.http.ResponseEntity<com.ashfaq.concepts.dto.Payment> com.ashfaq.concepts.dto.PaymentController.createPayment(com.ashfaq.concepts.dto.Payment) with 6 errors: [Field error in object 'payment' on field 'productName': rejected value []; codes [Size.payment.productName,Size.productName,Size.java.lang.String,Size]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [payment.productName,productName]; arguments []; default message [productName],50,2]; default message [Product name must be between 2 and 50 characters]] [Field error in object 'payment' on field 'transactionId': rejected value []; codes [NotBlank.payment.transactionId,NotBlank.transactionId,NotBlank.java.lang.String,NotBlank]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [payment.transactionId,transactionId]; arguments []; default message [transactionId]]; default message [Transaction ID is mandatory]] [Field error in object 'payment' on field 'productName': rejected value []; codes [NotBlank.payment.productName,NotBlank.productName,NotBlank.java.lang.String,NotBlank]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [payment.productName,productName]; arguments []; default message [productName]]; default message [Product name is mandatory]] [Field error in object 'payment' on field 'result': rejected value [null]; codes [NotNull.payment.result,NotNull.result,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [payment.result,result]; arguments []; default message [result]]; default message [Result is mandatory]] [Field error in object 'payment' on field 'cost': rejected value [0.0]; codes [DecimalMin.payment.cost,DecimalMin.cost,DecimalMin.double,DecimalMin]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [payment.cost,cost]; arguments []; default message [cost],false,0.0]; default message [Cost must be greater than 0]] [Field error in object 'payment' on field 'description': rejected value [A very long description that exceeds the 200 characters limit. A very long description that exceeds the 200 characters limit. A very long description that exceeds the 200 characters limit. A very long description that exceeds the 200 characters limit. A very long description that exceeds the 200 characters limit.]; codes [Size.payment.description,Size.description,Size.java.lang.String,Size]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [payment.description,description]; arguments []; default message [description],200,0]; default message [Description must be less than 200 characters]] \r\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor.resolveArgument(RequestResponseBodyMethodProcessor.java:144)\r\n\tat org.springframework.web.method.support.HandlerMethodArgumentResolverComposite.resolveArgument(HandlerMethodArgumentResolverComposite.java:122)\r\n\tat org.springframework.web.method.support.InvocableHandlerMethod.getMethodArgumentValues(InvocableHandlerMethod.java:224)\r\n\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:178)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:118)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:926)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:831)\r\n\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1089)\r\n\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:979)\r\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)\r\n\tat org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:914)\r\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:590)\r\n\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885)\r\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:195)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\r\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)\r\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)\r\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:482)\r\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115)\r\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)\r\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)\r\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:344)\r\n\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:389)\r\n\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)\r\n\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:904)\r\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1741)\r\n\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)\r\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1190)\r\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)\r\n\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)\r\n\tat java.base/java.lang.Thread.run(Thread.java:1583)\r\n",
//    "message": "Validation failed for object='payment'. Error count: 6",
//    "errors": [
//        {
//            "codes": [
//                "Size.payment.productName",
//                "Size.productName",
//                "Size.java.lang.String",
//                "Size"
//            ],
//            "arguments": [
//                {
//                    "codes": [
//                        "payment.productName",
//                        "productName"
//                    ],
//                    "arguments": null,
//                    "defaultMessage": "productName",
//                    "code": "productName"
//                },
//                50,
//                2
//            ],
//            "defaultMessage": "Product name must be between 2 and 50 characters",
//            "objectName": "payment",
//            "field": "productName",
//            "rejectedValue": "",
//            "bindingFailure": false,
//            "code": "Size"
//        },
//        {
//            "codes": [
//                "NotBlank.payment.transactionId",
//                "NotBlank.transactionId",
//                "NotBlank.java.lang.String",
//                "NotBlank"
//            ],
//            "arguments": [
//                {
//                    "codes": [
//                        "payment.transactionId",
//                        "transactionId"
//                    ],
//                    "arguments": null,
//                    "defaultMessage": "transactionId",
//                    "code": "transactionId"
//                }
//            ],
//            "defaultMessage": "Transaction ID is mandatory",
//            "objectName": "payment",
//            "field": "transactionId",
//            "rejectedValue": "",
//            "bindingFailure": false,
//            "code": "NotBlank"
//        },
//        {
//            "codes": [
//                "NotBlank.payment.productName",
//                "NotBlank.productName",
//                "NotBlank.java.lang.String",
//                "NotBlank"
//            ],
//            "arguments": [
//                {
//                    "codes": [
//                        "payment.productName",
//                        "productName"
//                    ],
//                    "arguments": null,
//                    "defaultMessage": "productName",
//                    "code": "productName"
//                }
//            ],
//            "defaultMessage": "Product name is mandatory",
//            "objectName": "payment",
//            "field": "productName",
//            "rejectedValue": "",
//            "bindingFailure": false,
//            "code": "NotBlank"
//        },
//        {
//            "codes": [
//                "NotNull.payment.result",
//                "NotNull.result",
//                "NotNull.java.lang.String",
//                "NotNull"
//            ],
//            "arguments": [
//                {
//                    "codes": [
//                        "payment.result",
//                        "result"
//                    ],
//                    "arguments": null,
//                    "defaultMessage": "result",
//                    "code": "result"
//                }
//            ],
//            "defaultMessage": "Result is mandatory",
//            "objectName": "payment",
//            "field": "result",
//            "rejectedValue": null,
//            "bindingFailure": false,
//            "code": "NotNull"
//        },
//        {
//            "codes": [
//                "DecimalMin.payment.cost",
//                "DecimalMin.cost",
//                "DecimalMin.double",
//                "DecimalMin"
//            ],
//            "arguments": [
//                {
//                    "codes": [
//                        "payment.cost",
//                        "cost"
//                    ],
//                    "arguments": null,
//                    "defaultMessage": "cost",
//                    "code": "cost"
//                },
//                false,
//                {
//                    "arguments": null,
//                    "codes": [
//                        "0.0"
//                    ],
//                    "defaultMessage": "0.0"
//                }
//            ],
//            "defaultMessage": "Cost must be greater than 0",
//            "objectName": "payment",
//            "field": "cost",
//            "rejectedValue": 0.0,
//            "bindingFailure": false,
//            "code": "DecimalMin"
//        },
//        {
//            "codes": [
//                "Size.payment.description",
//                "Size.description",
//                "Size.java.lang.String",
//                "Size"
//            ],
//            "arguments": [
//                {
//                    "codes": [
//                        "payment.description",
//                        "description"
//                    ],
//                    "arguments": null,
//                    "defaultMessage": "description",
//                    "code": "description"
//                },
//                200,
//                0
//            ],
//            "defaultMessage": "Description must be less than 200 characters",
//            "objectName": "payment",
//            "field": "description",
//            "rejectedValue": "A very long description that exceeds the 200 characters limit. A very long description that exceeds the 200 characters limit. A very long description that exceeds the 200 characters limit. A very long description that exceeds the 200 characters limit. A very long description that exceeds the 200 characters limit.",
//            "bindingFailure": false,
//            "code": "Size"
//        }
//    ],
//    "path": "/myapp/api/payments"
//}
