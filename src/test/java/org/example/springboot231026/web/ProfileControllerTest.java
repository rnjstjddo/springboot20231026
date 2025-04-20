package org.example.springboot231026.web;

import org.junit.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;


public class ProfileControllerTest {

    @Test
    public void real_profile이_조회된다(){
        //given
        String expectedProfile ="real";
        MockEnvironment mock =new MockEnvironment();
        mock.addActiveProfile(expectedProfile);
        mock.addActiveProfile("oauth");
        mock.addActiveProfile("real-db");

        ProfileController controller =new ProfileController(mock);

        //when
        String profile = controller.profiles();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    public void real_profile이_없으면_첫_번째가_조회된다(){
        //given
        String expectedProfile ="oauth";
        MockEnvironment mock = new MockEnvironment();
        mock.addActiveProfile(expectedProfile);
        mock.addActiveProfile("real-db");

        ProfileController controller = new ProfileController(mock);

        //when
        String profile = controller.profiles();

        //then
        assertThat(profile).isEqualTo(expectedProfile);

    }


    @Test
    public void active_profile이_없으면_default가_조회된다(){
        //given
        String expectedProfile ="default";
        MockEnvironment mock = new MockEnvironment();
        mock.addActiveProfile(expectedProfile);

        ProfileController controller = new ProfileController(mock);

        //when
        String profile = controller.profiles();

        //then
        assertThat(profile).isEqualTo(expectedProfile);

    }

}