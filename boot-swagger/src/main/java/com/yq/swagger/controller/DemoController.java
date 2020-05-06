package com.yq.swagger.controller;

import com.yq.kernel.enu.SexEnum;
import com.yq.kernel.model.ResultData;
import com.yq.swagger.controller.qo.UserQO;
import com.yq.swagger.controller.vo.UserVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *     demo controller
 *     测试：http://localhost:16080/swagger-ui.html
 * </p>
 * @author youq  2019/4/27 10:44
 * @常用注解说明：
 * @Api：用在类上，说明该类的作用
 * @ApiOperation：用在方法上，说明方法的作用
 * @ApiImplicitParams：用在方法上包含一组参数说明
 * @ApiImplicitParam：用在@ApiImplicitParams注解中，指定一个请求参数的各个方面
 *  paramType：参数放在哪个地方
 *      header-->请求参数的获取：@RequestHeader
 *      query-->请求参数的获取：@RequestParam
 *      path（用于restful接口）-->请求参数的获取：@PathVariable
 *      body（不常用）
 *      form（不常用）
 *  name：参数名
 *  dataType：参数类型
 *  required：参数是否必须传
 *  value：参数的意思
 *  defaultValue：参数的默认值
 * @ApiResponses：用于表示一组响应
 * @ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
 *  code：数字，例如400
 *  message：信息，例如"请求参数没填好"
 *  response：抛出异常的类
 * @ApiModel：描述一个Model的信息
 * （这种一般用在post创建的时候，使用@RequestBody这样的场景，请求参数无法使用@ApiImplicitParam注解进行描述的时候）
 * @ApiModelProperty：描述一个model的属性
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(value = "测试的controller", description = "RESTFUL API")
public class DemoController {

    @ApiOperation("测试方法1")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "username", dataType = "String", required = true, value = "用户名", defaultValue = "youq"),
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "String", required = true, value = "密码", defaultValue = "123456"),
            @ApiImplicitParam(paramType = "query", name = "sex", dataType = "String", required = true, value = "性别", defaultValue = "MALE", allowableValues = "MALE, FEMALE")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数不完整"),
            @ApiResponse(code = 404, message = "没有找到请求路径或者页面跳转路径不对"),
            @ApiResponse(code = 200, message = "成功", response = UserVO.class)
    })
    @PostMapping("/test1")
    public ResultData<UserVO> test1(@RequestHeader("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("sex") SexEnum sex) {
        log.info("username: {}, password: {}, sex: {}", username, password, sex);
        return ResultData.success(
                UserVO.builder()
                        .username(username)
                        .password(password)
                        .sex(sex)
                        .build()
        );
    }

    @ApiOperation(value = "测试方法2", notes = "请求参数实体测试")
    @ApiImplicitParam(name = "qo", value = "请求参数实体", required = true, dataType = "UserQO")
    @PostMapping("/test2")
    public ResultData<?> test2(@RequestBody UserQO qo) {
        log.info("请求参数：{}", qo);
        return ResultData.success();
    }

}
