
$(document).ready(function(){

    $("input[type='file']").on("change",function(){
        //-console.log("uploadEx.html 진입 change()이벤트 진입");

        var formData = new FormData();

        var files = $("input[type='file']");
        var multipartFiles = files[0].files;

        for(var i=0; i<multipartFiles.length; i++){
            formData.append("multipartFiles", multipartFiles[i]);
        }

        $.ajax({
            url:'/uploadAjax',
            method:'post',
            dataType:'json',//받을 데이터형태
            data:formData,
            contentType:false,
            processData:false,
            success: function(result){
                //-console.log("uploadEx.html 진입 change 진입 -success() 진입");
                //서버로 받은 결과로 화면처리
                showUploadImages(result);
            },
            error: function(jqXHR, textStatus, errorThrown){
                //-console.log("uploadEx.html 진입 change 진입 -error() 진입");
                //-console.log(jqXHR);
                //-console.log(textStatus);
                //-console.log(errorThrown);
            }//error
        });//ajax
    });//클릭이벤트함수


    function showUploadImages(arr){
        //-console.log("uploadEx.html 진입 - showUploadImages() 함수 진입");
        //-console.log(arr);
        var uploadResultDiv = $(".uploadResult");

        $.each(arr, function(index, obj){
                //console.log(index, obj);
                uploadResultDiv.append(
                `<br><div><img src='/display?fileName=${obj.thumbnailURL}'><br><b>${obj.fileName}</b>
                <a type="button" class="imageRemoveBtn" style="color:#cc0000; text-decoration:none;" data-name="${obj.imageURL}">X</a></div>`);
        });//each문*/
    }//showUploadImages

    //이미지 삭제시 이벤트위임함수사용
    $(".uploadResult").on("click", ".imageRemoveBtn", function(e){
        //-console.log("uploadEx.html 진입 - 업로드 이미지 삭제버튼 클릭진입(이벤트 위임함수적용)");

        var target = $(this)
        var name =target.data("name");
        //-console.log(name);

        $.post("/removeFile", {fileName:name},function(result){

            //-console.log("uploadEx.html 진입 - 업로드 이미지 삭제버튼 클릭진입(이벤트 위임함수적용) - $.post 처리후 받은 데이터 -> "+ result);

            if(result === true){
                target.closest("div").remove();
            }

        });
    });

});//$(document)
