package com.peixoto.api.services;

import com.peixoto.api.entities.Users;
import com.peixoto.api.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Unidade - User Details Service")
public class UserDetailServiceTest {

    @InjectMocks
    private UserDetailService userDetailService;

    @Mock
    private UserRepository mockUserRepository;

    @Test
    @DisplayName("Deve retornar o usuário buscado")
    void loadUserByUsername_shouldReturnUsername() {
        Mockito.when(mockUserRepository.findByUsername(Mockito.anyString()))
                .thenReturn(new Users());

        assertThat(userDetailService.loadUserByUsername("username")).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção caso o usuário procurado não existir")
    void loadUserByUsername_shouldThrowUsernameNotFoundException() {
        assertThatThrownBy(() -> userDetailService.loadUserByUsername("username"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User username not found");
    }

}
