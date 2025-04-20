
console.log("member.js 진입");
//못읽는다.
//var getLink =[[${pageRequestDto.getLink()}]];

//var getLink = $("#joinBtn").data("getlink");

//var getLink = $("#getlink").val();
//console.log(getLink);

$("#joinBnt").on("click", (event)=>{
    console.log("member.js 회원가입버튼 클릭이벤트진입");
    //_this.join();

    fetch("/member/join", {
        method:'post',
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify({
            username: $("#username").val(),
            password: $("#password").val(),
            email : $("#email").val()
        }),
    })
    .then((response) => {
        if (response.ok === true) {
          return response.json();
        }
        throw new Error('에러 발생!'); //reponse.ok가 true가 아닐 경우(네트워크 응답이 올바르지 않은 경우) error를 throw
      })
    .catch((error) => console.log(error)) //throw된 error를 받아서 console에 출력
    .then( (data)=>{
        console.log("member.js 회원가입버튼 클릭이벤트진입 fetch -then() 진입");
        console.log(data);
        alert("회원가입이 완료되었습니다.로그인해주세요!");
        location.replace("/member/login");
    });

    /*.then((data) => {
        if (data.message === 'login success') {
          localStorage.setItem('TOKEN', data.token);
          alert('로그인 성공');
        } else {
          alert('로그인 실패');
        }
      });
    */
    /*.then((response) => {
        console.log("member.js 회원가입버튼 클릭이벤트진입 fetch() -then() 진입");
        console.log(response);
        alert("회원가입이 완료되었습니다.로그인해주세요!");
        location.replace("/member/login");
    });*/
});
