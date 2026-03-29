package org.example.banco.selenium;

import org.example.banco.selenium.core.BaseTest;
import org.example.banco.selenium.pages.AccountCreatedPage;
import org.example.banco.selenium.pages.AccountInformationPage;
import org.example.banco.selenium.pages.HomePage;
import org.example.banco.selenium.pages.SignupLoginPage;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Testes extends BaseTest {

    private String randomEmail() {
        return "qa_" + UUID.randomUUID() + "@teste.com";
    }

    @Test
    void deveCadastrarNovoUsuarioELogarComSucesso() {
        String testName = "deveCadastrarNovoUsuarioELogarComSucesso";
        String email = randomEmail();
        String password = "SenhaForte123";

        try {
            HomePage home = new HomePage(driver);
            home.open();
            assertTrue(home.isHomeVisible(), "Home não carregou corretamente");

            SignupLoginPage signupLogin = home.clickSignupLogin();
            assertTrue(signupLogin.isSignupFormVisible(), "'New User Signup!' não está visível");

            AccountInformationPage accountInfo =
                    signupLogin.startSignup("QA Automation", email);

            AccountCreatedPage createdPage =
                    accountInfo.fillFormAndSubmit(password);

            assertTrue(createdPage.isAccountCreatedVisible(),
                    "Mensagem 'ACCOUNT CREATED!' não visível");

            HomePage homeAfterSignup = createdPage.clickContinue();
            assertTrue(homeAfterSignup.isLoggedIn(),
                    "'Logged in as' não está visível após cadastro");

            homeAfterSignup.logout();

            SignupLoginPage loginPage = homeAfterSignup.clickSignupLogin();
            loginPage.login(email, password);

            HomePage homeAfterLogin = new HomePage(driver);
            assertTrue(homeAfterLogin.isLoggedIn(),
                    "Usuário está logado após login com credenciais válidas");

        } catch (Exception e) {
            takeScreenshot(testName);
            throw e;
        }
    }

    @Test
    void deveMostrarErroAoFazerLoginComCredenciaisInvalidas() {
        String testName = "deveMostrarErroAoFazerLoginComCredenciaisInvalidas";

        try {
            HomePage home = new HomePage(driver);
            home.open();
            assertTrue(home.isHomeVisible(), "Home não carregou corretamente");

            SignupLoginPage loginPage = home.clickSignupLogin();

            loginPage.login("usuario_inexistente@teste.com", "senhaErrada123");

            String error = loginPage.getLoginErrorMessage();
            assertEquals("Your email or password is incorrect!", error);

        } catch (Exception e) {
            takeScreenshot(testName);
            throw e;
        }
    }


}
