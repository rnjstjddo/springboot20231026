var registerjs = {
    init: function(){

            var registerForm=$("form");

            //var regex = new RegExp("(.*?)\.(exe|sh|zip|alz|tiff|webp)$");
            var regex = new RegExp("(.*?)\.(jpg|bmp|jpeg|png|gif)$");

            var maxSize= 10485760;
            var imageDataArray =[];

        // --------------------------------------------------------------------------
            function checkExtension(fileName, fileSize){
                //- console.log("register.html 첨부파일 checkExtension() 정규표현식 검증함수 진입");

                if(fileSize >= maxSize){
                    alert("파일사이즈를 초과했습니다.");
                    $("input[type=file]").val("")
                    return false;
                }

                if(!regex.test(fileName)){
                    alert("jpg|bmp|jpeg|png|gif 이외의 파일은 업로드 할수없습니다");
                    $("input[type=file]").val("")
                    return false;
                }
                return true;// 위의 2개 조건이 아닌경우 true던진다.
            }

        // --------------------------------------------------------------------------
            //첨부파일이미지 change() 이벤트
            $("input[type='file']").change(function(){
                //-console.log("register.html 첨부파일 change() 이벤트 진입");

                var formData = new FormData();
                var fileTag= $(this);

                var multipartFiles= fileTag[0].files;

                var appended= false;

                $.each(multipartFiles, function(index, obj){
                    if(!checkExtension(obj.name, obj.size)){
                        //-console.log("register.html 첨부파일 change() 이벤트 진입 - 파일정규표현식 부적합할때 진입");
                        $("#file").val("");
                        return false;
                    }

                    formData.append("multipartFiles",obj);
                    appended = true;
                });//each

                if(!appended){
                    //-console.log("register.html 첨부파일 change() 이벤트 진입 - 파일정규표현식 부적합할때 진입 $.each 함수 나와서 return 던진다.");
                    return;
                }// each내부여서 밖으로 나와서 change()이벤트함수 내에서 처리

                $.ajax({
                    url:'/dogselluploadAjax',
                    processData:false,
                    contentType: false,
                    data:formData,
                    type:'post',
                    dataType:'json',

                    success:function(result){
                        //-console.log("register.html 첨부파일 change() 이벤트 진입 - $.ajax success() 진입");
                        //-console.log(result);
                        //-console.log("첨부파일 업로드가 완료되었습니다. 분양글 등록버튼을 클릭하시면 글등록가능합니다!")
                        showResult(result);//서버에서 받은 이미정보로 이미지 화면에 보여주기
                    },
                    error: function(jqXHR, testStatus, errorThrown){
                        //-console.log("register.html 첨부파일 change() 이벤트 진입 - $.ajax error() 진입");
                        alert("첨부파일이 업로드되지 않았습니다. 다른 파일을 선택해주세요!")
                        //-console.log(testStatus);
                    }

                });//$.ajax
            });//change() 함수

        // --------------------------------------------------------------------------
            $("#age").keyup(function(){
                //-console.log("register.html keyup() 이벤트진입 age경우 소수점 1자리 검증진입");

                var age =$(this).val();
                //-console.log("register.html keyup() 이벤트진입 age경우 소수점 1자리 검증 age값 -> "+age);
                var verify = false;
                if (/^[\d]*\.?[\d]{0,1}$/.test(age)) {
                   verify = true;
                   return;
                }

                if(!verify){
                    //-console.log("register.html keyup() 이벤트진입 age경우 소수점 1자리 검증 - 검증실패시");
                    alert("나이는 소수점 첫째자리까지만 입력해주세요");
                    return;
                }
            });//나이

        // --------------------------------------------------------------------------
            $("#weight").keyup(function(){
                //-console.log("register.html keyup() 이벤트진입 weight경우 소수점 1자리 검증진입");

                var weight =$(this).val();

                //-console.log("register.html keyup() 이벤트진입 weight경우 소수점 1자리 검증 -weight -> "+weight);

                var verify = false;
                if (/^[\d]*\.?[\d]{0,1}$/.test(weight)) {
                   verify = true;
                   return;
                }

                if(!verify){
                    //-console.log("register.html keyup() 이벤트진입 weight 소수점 1자리 검증 - 검증실패시");
                    alert("몸무게는 소수점 첫째자리까지만 입력해주세요");
                    return;
                }
            });//몸무게


        // --------------------------------------------------------------------------
             $("#gender").focusout(function(){
                //-console.log("register.html focusout() 이벤트진입 gender경우 한글1문자 검증진입");

                var gender =$(this).val();
                //-console.log("register.html focusout() 이벤트진입 gender경우 한글1문자 검증진입 - gener값 -> "+gender);

                var verify = false;
                if (/^[ㄱ-힣]$/.test(gender)) {
                   verify = true;
                   return;
                }

                if(!verify){
                    //-console.log("register.html keyup() 이벤트진입 gender경우 한글1문자 검증 - 검증실패시");
                    alert("성별은 한글문자 1개로 입력해주세요");
                    return;
                }
            });//성별

        // --------------------------------------------------------------------------
            function showResult(uploadResultArr){
                //-console.log("register.html showResult()함수진입 - $.ajax success() 서버에서 전달받은 첨부파일데이터로 화면에서 이미지 보여주기");

                var uploadUl= $(".uploadResult ul");
                //등록버튼 클릭시 이미지정보도 서버의 파라미터로 들어가야하기에 미리 저장한다. 삭제할때는 제거시킨다. 반드시


                var str="";
                $.each(uploadResultArr, function(index, obj){
                    //-console.log("register.html showResult()함수진입 - $.ajax success() 서버에서 전달받은 첨부파일데이터로 화면에서 이미지 보여주기 -$.each 진입");
                    //-console.log(obj); //fileName, uuid, folderPath;
                    str+=`<li data-name="${obj.fileName}" data-path="${obj.folderPath}" data-uuid="${obj.uuid}"
                             style='list-style-type:none;float:left;'>
                        <br>
                        <a type="button" data-file=${obj.imageURL} style="color:#cc0000; text-decoration:none;">
                        <img src="/dogselldisplay?fileName=${obj.thumbnailURL}" style="height:200px;width:auto;">
                        <br>X</a>
                        </li>`

                    var fileNameLi = obj.fileName;
                    var folderPathLi = obj.folderPath;
                    var uuidLi = obj.uuid;

                    //-console.log("register.html showResult()함수진입 - $.ajax success() obj.imgName -> "+ fileNameLi);
                    //-console.log("register.html showResult()함수진입 - $.ajax success() obj.path -> "+ folderPathLi);
                    //-console.log("register.html showResult()함수진입 - $.ajax success() obj.uuid -> "+ uuidLi);

                    imageDataArray.push({imgName:fileNameLi,path:folderPathLi,uuid:uuidLi});
                    //-console.log("register.html showResult()함수진입 - $.ajax success() imageDataArray 배열 출력");

                    //-console.log(imageDataArray);
                });//$.each

                uploadUl.append(str);
                //-console.log(str);
            }//showResult()


        // --------------------------------------------------------------------------
            $(".uploadResult ").on("click", "li", function(){
                //-console.log("register.html 첨부파일 이미지 삭제버튼 클릭이벤트 진입 이벤트위임방식진행");

                var target = $(this);
                var dataFile = target.find("a").data("file");

                var fileNameData = target.data("name");
                var uuidData = target.data("uuid");
                var folderPathData = target.data("path");
                //-console.log("register.html 첨부파일 이미지 삭제버튼 클릭이벤트 진입 이벤트위임방식진행 -dataFile "+dataFile);
                //-console.log("register.html 첨부파일 이미지 삭제버튼 클릭이벤트 진입 이벤트위임방식진행 -fileNameData "+fileNameData);
                //-console.log("register.html 첨부파일 이미지 삭제버튼 클릭이벤트 진입 이벤트위임방식진행 -uuidData "+uuidData);
                //-console.log("register.html 첨부파일 이미지 삭제버튼 클릭이벤트 진입 이벤트위임방식진행 -folderPathData "+folderPathData);


                //i => i.name == "강호동"
                var inumIndex = imageDataArray.findIndex(i=> i.uuid == "uuidData");
                //-console.log("register.html 첨부파일 이미지 삭제버튼 클릭이벤트 진입 이벤트위임방식진행 - 삭제할이미지 번호 inum 의 indexOf() 적용-> "+ inumIndex);

                $.ajax({
                    url: '/dogsellremoveFile',
                    data: {fileName:dataFile},
                    dataType:'text',//받은것
                    type:'post',
                    success: function(result){
                        //-console.log("register.html 첨부파일 이미지 삭제버튼 클릭이벤트 진입 이벤트위임방식진행 -ajax success() 진입");
                        //-console.log(result);
                        //객체배열에서도 삭제하기
                        imageDataArray.splice(inumIndex,1);
                        //-console.log("register.html 첨부파일 이미지 삭제버튼 클릭이벤트 진입 이벤트위임방식진행 -ajax success() 진입 -삭제후 imageDataArray배열객체 확인");

                        //-console.log(imageDataArray);
                        target.remove();

                        var filecheck =$(".uploadResult ").find("li").size();
                        //-console.log("dogsellregister.js 글등록에서 업로드한 파일삭제시 모두 삭제할경우 파일선택에서 이름도 초기화시키키 먼저 li태그 존재하는지 확인");
                        //-console.log(filecheck);
                        if(filecheck == 0){
                            //-console.log("첨부파일이 한개도 없기에 초기화시킨다.")
                            $("#file").val("");
                            return;
                        }
                    }

                });//ajax
            });//화면 이미지 삭제버튼 클릭

        // --------------------------------------------------------------------------
        $("#registerBtn").on("click", function(e){
            //-console.log("register.html 등록하기 버튼이벤트 진입 ");
            e.preventDefault();

            var registerForm = $("form");
            var ageverify = false;
            var weightverify = false;
            var genderverify = false;

            var blank_pattern = /\s/g;


                var titleVal = $('#title').val();
                var weightVal = $('#weight').val(); //유효성
                var genderVal = $('#gender').val();//유효성
                var writerVal = $('#writer').val();
                var typeVal = $('#type').val();
                var ageVal = $('#age').val();//유효성
                var nameVal = $('#name').val();
                var healthVal = $('#health').val();
                var priceVal = $('#price').val();//유효성
                var contentVal = $('#content').val();
                var fileVal = $('#file').val();

/*
                if (titleVal == '' || blank_pattern.test(titleVal) == true ) {
                  alert('게시물 제목을 입력하시고 공백을 제거해주세요!');
                        setTimeout(function(){
                        $("#title").focus();
                    });
                }
*/

                if (weightVal == '' || blank_pattern.test(weightVal) == true ) {
                  alert('강아지무게를 입력하시고 (소수점 첫째자리까지) 공백을 제거해주세요! ');
                    setTimeout(function(){
                        $("#weight").focus();
                    });
                }
                if (genderVal == ''|| blank_pattern.test(genderVal) == true) {
                  alert('강아지 성별을 입력해주시고 (한 글자로) 공백을 제거해주세요!');
                    setTimeout(function(){
                        $("#gender").focus();
                    });
                }
              /*  if (typeVal == ''|| blank_pattern.test(typeVal) == true) {
                  alert('강아지 품종을 입력해주시고 공백을 제거해주세요!');
                    setTimeout(function(){
                        $("#type").focus();
                    });
                }*/
                if (ageVal == ''|| blank_pattern.test(ageVal) == true) {
                  alert('강아지 나이를 입력해주시고 (소수점 첫째자리까지) 공백을 제거해주세요');
                    setTimeout(function(){
                        $("#age").focus();
                    });
                }
/*
                if (nameVal == '' || blank_pattern.test(nameVal) == true) {
                  alert('강아지 이름을 입력해주시고 공백을 제거해주세요!')
                    setTimeout(function(){
                        $("#name").focus();
                    });
                }
*/

/*
                if (healthVal == ''|| blank_pattern.test(healthVal) == true) {
                  alert('강아지 예방접종명을 입력해주시고 공백을 제거해주세요!');
                    setTimeout(function(){
                        $("#health").focus();
                    });
                }
*/

                if (priceVal == '' || blank_pattern.test(priceVal) == true) {
                  alert('강아지 분양가격을 입력해주시고 (단위: 만원) 공백을 제거해주세요')
                    setTimeout(function(){
                        $("#price").focus();
                    });
                }
                if (contentVal == '') {
                  alert('강아지 특징을 입력해주세요!');
                    setTimeout(function(){
                        $("#content").focus();
                    });
                }


                if (/^[0-9]*$/.test(priceVal)) {
                   priceverify = true;
                   //return true;
                }

                if(!priceverify){
                    //-console.log("register.html 등록버튼클릭시 price경우 숫자만 - 검증실패시");
                    alert("분양가격은 단위가 만원입니다.");
                    setTimeout(function(){
                        $("#price").focus();
                    });
                }


                if (/^[\d]*\.?[\d]{0,1}$/.test(ageVal)) {
                   ageverify = true;
                   //return true;
                }

                if(!ageverify){
                    //-console.log("register.html 등록버튼클릭시 age경우 소수점 1자리 검증 - 검증실패시");
                    alert("나이는 소수점 첫째자리까지만 입력해주세요");
                    setTimeout(function(){
                        $("#age").focus();
                    });
                }

                if (/^[\d]*\.?[\d]{0,1}$/.test(weightVal)) {
                   weightverify = true;
                   //return true;
                }

                if(!weightverify){
                    //-console.log("register.html 등록버튼클릭시 weight경우 소수점 1자리 검증 - 검증실패시");
                    alert("무게는 소수점 첫째자리까지만 입력해주세요");
                    setTimeout(function(){
                        $("#weight").focus();
                    });
                }

                if (/^[가-힣]$/.test(genderVal) || /^[가-힣]$/.test(genderVal)) {
                   genderverify = true;
                   //return true;
                }

                if(!genderverify){
                    //-console.log("register.html 등록버튼클릭시 gender경우 한글1문자 검증 - 검증실패시");
                    alert("성별은 한글문자 1개로 입력해주세요");
                    setTimeout(function(){
                        $("#gender").focus();
                    });
                }

                //강아지 첨부파일 없을경우
                if (fileVal == '') {
                  alert('분양할 강아지 사진을 첨부해주세요!');
                    setTimeout(function(){
                        $("#file").focus();
                    });
                }


            if( genderverify && ageverify && weightverify &&priceverify){
                //-console.log("register.html 등록하기 버튼이벤트 진입 - 모든검증 true");

                //이미지정보추출

                //-console.log("register.html 등록하기 버튼이벤트 진입 - imageDataArray 배열객체 -> ");
                //-console.log(imageDataArray);

        /*
                var str="";
                $.each(uploadResultArr, function(index, obj){
                    str+=`<li data-name=${obj.imgName} data-path=${obj.path} data-uuid=${obj.uuid}
                            data-inum=${obj.inum} style='list-style-type:none;float:left;'>
                        <br>
                        <a type="button" data-file=${obj.imageURL} style="color:#cc0000; text-decoration:none;">
                        <img src="/dogselldisplay?fileName=${obj.thumbnailURL}">
                        <br>X</a>
                        </li>`
                });//each*/
        //item : [{  a0 : "a0",  b0 : "b0",  c0 : "c0",  d0 : "d0"


                var data={
                    title:titleVal, weight:weightVal, gender:genderVal, writer:writerVal, type:typeVal, age:ageVal,
                    name:nameVal, health:healthVal, price:priceVal, content:contentVal,
                    dsiDtoList:imageDataArray
                    //dsiDto:imageDataArray
                    //dsiDtoList:[{fileName:imageDataArray["fileName"],folderPath:imageDataArray["folderPath"],uuid:imageDataArray["uuid"] }]
                    //서버의 객체의 멤버변수에 맞춘다.
                    //dsImageList:imageDataArray
                    //dsImageList.inum:inum, dsIorgmageList.uuid:uuid, dsImageList.imgName:imgName, dsImageList.path:path
                };
                //-console.log(data);

                $.ajax({
                    url:'/dogsell/register',
                    method:'post',
                    dataType:'json',
                    contentType:'application/json;charset=utf-8',
                    data:JSON.stringify(data)
                })
                .done(function(result){
                    //-console.log("register.html 등록하기 버튼이벤트 진입 -ajax처리 api서버로 -done() 진입");
                    alert(result+"번 게시글이 등록되었습니다.");
                    location.replace(`/dogsell/list`);
                })
                .fail(function(error){
                    //-console.log("register.html 등록하기 버튼이벤트 진입 -ajax처리 api서버로 -fail() 진입");
                    //-console.log(error);
                });//ajax

            }//if
        });//등록하기버튼 이벤트처리


    }//init 함수지정
}//registerjs 스코프객체치정
