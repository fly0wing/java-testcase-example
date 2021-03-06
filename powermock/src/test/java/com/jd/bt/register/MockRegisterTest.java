package com.jd.bt.register;

import com.jd.bt.mock.AbstractMockRunner;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

/**
 * User: 吴海旭
 * Date: 2016-11-26
 * Time: 下午10:00
 */
@PrepareForTest(RegisterController.class)
public class MockRegisterTest extends AbstractMockRunner {

    @Test
    public void testRegister() {
        RegisterDTO registerDTO = constructInstance();
        // 使用局部mock,这个例子中我只要mock verifyImageCode这个方法,其他的方法还是按我定义的来走。
        RegisterController mockController = PowerMockito.spy(new RegisterController());

        PowerMockito.when(mockController.verifyImageCode("1234")).then(a -> {
            System.out.println("验证图形码成功");
            return true;
        });
//        PowerMockito.when(mockController.verifyImageCode("1234")).thenReturn(true);

        String result = mockController.register(registerDTO);
        Assert.assertEquals("success", result);
    }

    @Test
    public void testRegister2() throws Exception {
        RegisterDTO registerDTO = constructInstance();
        RegisterController mockController = PowerMock.createPartialMock(RegisterController.class, "verifyImageCode");
        // 调用register方法的时候,它调用了此类的其他方法(verifyImageCode),得使用expectPrivate
        PowerMock.expectPrivate(mockController, "verifyImageCode", "1234").andAnswer(() -> {
            System.out.println("验证图形码成功");
            return true;
        });
        PowerMock.replay(mockController);

        String result = mockController.register(registerDTO);
        Assert.assertEquals("success", result);
    }

    private RegisterDTO constructInstance() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUserName("benjamin");
        registerDTO.setPassword("123456");
        registerDTO.setPhoneVerifyCode("6523");
        registerDTO.setImageVerifyCode("1234");
        return registerDTO;
    }
}
