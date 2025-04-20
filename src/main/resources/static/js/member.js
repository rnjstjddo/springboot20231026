
//-console.log("member.js 진입");
//못읽는다.
//var getLink =[[${pageRequestDto.getLink()}]];

//var getLink = $("#joinBtn").data("getlink");

//var getLink = $("#getlink").val();
//console.log(getLink);

var memberObject ={

    init: function(){
        //-console.log("member.js 객체 memberObject의 init 함수진입");
        var _this =this; //객체를 의미한다.

        //아이디중복확인 on이벤트진입
        $("#namecheck").on("click", function(){
            //-console.log("member-join.html 회원가입 중 아이디중복확인 진입");

            var nameVal = $("#name").val();
            var data ={name:nameVal};
            //-console.log("member-join.html 회원가입 중 아이디중복확인 진입 입력받은 아이디값 -> "+ nameVal);

            $.ajax({
                url:'/member/join/check',
                method:'post',
                dataType: 'json',
                data:JSON.stringify(data),
                contentType:'application/json;charset=utf-8',

                success: function(result){
                    //-console.log("member-join.html 회원가입 중 아이디중복확인 결과 -> "+result);

                    if(result == true){
                        //-console.log("member-join.html 회원가입 중 아이디중복확인 결과 -> 사용가능")
                        alert("아이디 사용가능합니다!")
                        $("#name").attr("readonly",true);
                        return;
                    }
                    if(result == false){
                        //-console.log("member-join.html 회원가입 중 아이디중복확인 결과 -> 중복됨")
                        alert("아이디가 중복됩니다. 다른 아이디로 변경해주세요!");
                        $("#name").focus();
                        return;
                    }
                },

                fail: function(){
                    //-console.log("member-join.html 회원가입 중 아이디중복확인 중 에러발생 현재페이지 replace() ")
                    location.replace(`/member/join`)
                }
            });//끝 $.ajax
        });//끝 on클릭이벤트진입 아이디중복확인


        $("#joinBtn").on("click", ()=>{
            //-console.log("member.js 의 init 함수진입 -회원가입버튼 클릭이벤트진입");
            _this.join();

/*            fetch("/member/join", {
                method:'post',
                headers:{
                    "Content-Type":"application/json"
                },
                body:JSON.stringify({

                    username: $("#username").val(),
                    password: $("#password").val(),
                    email : $("#email").val()
                }),
            }).then(() => {
                //-console.log("member.js 의 init 함수진입 -회원가입버튼 클릭이벤트진입 fetch() -then() 진입");
                alert("회원가입이 완료되었습니다.로그인해주세요!");
                location.replace("/member/login");
            });*/
        });

        $("#updateJoinBtn").on("click", ()=>{
            //-console.log("member.js 의 init 함수진입 -회원정보 수정버튼 클릭이벤트진입");
            _this.updateJoin();
        });//updateJoinBtn 클릭이벤트함수


        $("#updateSocialJoinBtn").on("click", ()=>{
            //-console.log("member.js 의 init 함수진입 -회원정보 수정버튼 클릭이벤트진입");
            _this.updateSocialJoin();
        });//updateSocialJoinBtn 클릭이벤트함수


    },//init

    join : function(){
        //-console.log("member.js 객체 memberObject의 join 함수진입 - 회원가입에서 가입버튼 클릭한경우 진입");

        var readonlyCheck =$("#name").attr("readonly");
        //-console.log("id가 name의 readonly속성값 확인 -> "+readonlyCheck)
        var passwordval =$("#password").val();
        var password2val =$("#password2").val();

        //var emailval =$("#email").val();

        var emailVal= $("#email").val();

        if(emailVal=="" || emailVal== null){
            //-console.log("member.js 회원가입에서 email칸이 비어있는 경우 진입");
            alert("이메일을 입력해주세요!");
            $("#email").focus();
            return;
        }

        const pattern = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-za-z0-9\-]+/;

        if(pattern.test(emailVal) === false) {
            alert("이메일 형식이 맞지 않습니다. 다시 확인해주세요!");
            $("#email").focus();
            return;
        }

        if(passwordval != password2val){
            //-console.log("member.js 객체 memberObject의 join 함수진입 비밀번호가 일치하지 않는경우 진입 ");
            alert("비밀번호가 일치하지 않습니다. 다시 한번 확인해주세요!")
            $("#passwordval").focus();
            return;
        }

        if(!readonlyCheck){
            //-console.log("member.js 객체 memberObject의 join 함수진입 아이디중복확인 체크가 안된경우 진입 ");
            alert("아이디 중복확인을 해주세요!")
            $("#name").focus();
            return;
        }

        var member ={
            name: $("#name").val(),
            password: $("#password").val(),
            email : $("#email").val()
        }

        $.ajax({
            type:'post',
            url:'/member/join',
            dataType:'text',
            data:JSON.stringify(member),
            contentType:'application/json;charset=utf-8'
        })
        .done(function(response){
            //-console.log("member.js 객체 memberObject의 join 함수진입 - $.ajax -done() 진입");
            //-console.log(response);
            //location="/member/login";
            alert("회원가입이 완료되었습니다. 로그인을 진행해주세요!")
            location.replace("/member/login");
        })
        .fail(function(error){
            //-console.log("member.js 객체 memberObject의 join 함수진입 - $.ajax -fail() 진입");
            //-console.log(error);
            alert("회원가입에 실패했습니다. 다시 회원가입을 해주세요!")
            location.replace("/member/join");
        });
    },//join


    updateJoin : function(){
        //-console.log("member.js 객체 memberObject의 updateJoin 함수진입");

        const passwordcheck = $("#passwordcheck").val()
        const password = $("#password").val()

        //if( !(passwordcheck.trim() == "" && password.trim() =="") && passwordcheck != password){
        if( !($.trim(passwordcheck) == "" && $.trim(password) =="") && passwordcheck != password){

            alert("비밀번호 변경을 원하시면 비밀번호 확인란과 일치시켜주세요");
            //-console.log(passwordcheck)
            //-console.log(password)
            return;
        }

         //if(  passwordcheck == password && (passwordcheck.trim() == "" && password.trim() =="") ){
         if( ($.trim(passwordcheck) == "" && $.trim(password) =="") && passwordcheck == password ){

            //-console.log("member.js 객체 memberObject의 updateJoin 함수진입 - 비밀번호를 수정하지 않은경우 진입");
            alert("회원정보를 수정하지 않으셨습니다. 메인페이지로 이동합니다!")
            location.replace("/home/home");
        }


        //if(  passwordcheck == password || (passwordcheck.trim() != "" && password.trim() !="") ){
        if(  ($.trim(passwordcheck) != "" && $.trim(password) !="") && passwordcheck == password ){

            //-console.log("member.js 객체 memberObject의 updateJoin 함수진입 비밀번호를 수정한 경우 진입");

            var updatemember ={
                //name: $("#name").val().trim(),
                //password: $("#password").val().trim()
                 name: $.trim($("#name").val()),
                 password: $.trim($("#passwordcheck").val()) //여기서 name과 password는 JoinDTO로
                 //컨트롤러의 핸들러메소드에서 @RequestBody있는 객체에 들어가는값이되기에 같은 변수명이어야한다.
            }
            if(confirm("해당 비밀번호로 수정하시겠습니까?")){

                $.ajax({
                    type:'put',
                    url:'/member/updateJoin',
                    dataType:'text',
                    data:JSON.stringify(updatemember),
                    contentType:'application/json;charset=utf-8'
                })
                .done(function(response){
                    //-console.log("member.js 객체 memberObject의 updateJoin 함수진입 - $.ajax -done() 진입 비밀번호 수정");
                    location.replace("/member/login");
                })
                .fail(function(error){
                    //-console.log("member.js 객체 memberObject의 updateJoin 함수진입 - $.ajax -fail() 진입 비밀번호 수정");
                    //-console.log(error);
                });
            }//끝 if문 confirm()
        }
    },//updateJoin

    updateSocialJoin : function(){
        //-console.log("member.js 객체 memberObject의 updateSocialJoin 함수진입");

        var updatesocialmember ={
            name: $("#name").val(),
            //password: $("#password").val()
        }

        var name= $("#name").val();

        $.ajax({
            type:'put',
            url:'/member/updateSocialJoin',
            dataType:'text',
            data:JSON.stringify(updatesocialmember),
            contentType:'application/json;charset=utf-8'
        })
        .done(function(response){
            //-console.log("member.js 객체 memberObject의 updateSocialJoin 함수진입 - $.ajax -done() 진입");
            //-console.log(response);
            if(response ==="exist"){
                alert("동일한 닉네임을 가진 회원이 존재합니다. 다른 닉네임을 선택해주세요!");
                return;
            }else{
                alert(response +" 로 닉네임이 변경되었습니다!");

            }
            //location="/member/login";
            location.replace(`/home/home`);
        })
        .fail(function(error){
            //-console.log("member.js 객체 memberObject의 updateSocialJoin 함수진입 - $.ajax -fail() 진입");
            //-console.log(error);
        });
    },//updateSocialJoin


}//memberObject
memberObject.init();