
$(document).ready(function(){

    $('.ui.dropdown').dropdown()
        
})

async function profileImgUpload(userID){
    let fileInput = $(`#profileImg`)[0].files[0];

    let formData = new FormData();
    formData.append("file",fileInput);


    await $.ajax({
        type : "post",
        url : `/users/uploads/images/${userID}`,
        contentType: false,
        processData:false,
        data : formData,
        beforeSend:()=>{
            $("#loading__container").attr("style","display:inline-block;");
            $("#signup__button").attr("disabled",true);
        },
        error : json => {
            $("#loading__container").attr("style","display:none;");
            let errorMsg = json.responseJSON.message;
            alert(errorMsg);
             $("#signup__button").attr("disabled",false);
            //await UserDelete(userID);

            return 0;
        }
    })

    return userID;
}

async function signup(){

    let $fileInput = $(`#profileImg`)[0].files[0];

    if($fileInput === undefined){
        alert("프로필 사진을 같이 등록해주세요.");
        return ;
    }

    const $password      = $("#signup__password").val();
    const $passwordCheck = $("#signup__password-check").val();
    const $name          = $("#signup__name").val();
    const $email         = emailAdd();

    if($email === undefined){
        return;
    }else if($password !== $passwordCheck){
        alert("비밀번호가 일치하지 않습니다.");
        return;
    }else if($name ===  ""){
        alert("이름을 입력해주세요.")
        return;
    }else{
        
        const $userData = {
            email : $email,
            password : $password,
            name : $name,
        }

        let response = await $.ajax({
            type: 'post',
            url : `http://localhost:8080/users/signups`,
            contentType : 'application/json',
            data : JSON.stringify($userData),
            beforeSend:()=>{
                $("#loading__container").attr("style","display:inline-block;");
                $("#signup__button").attr("disabled",true);
            },
            error:()=>{
                $("#signup__button").attr("disabled",false);
                alert("회원가입 에러");
            },
            complete:()=>{
                $("#loading__container").attr("style","display:none;");
            }
        })


        if(response.message != null){
            $("#signup__button").attr("disabled",false);
            alert(response.message)
            return;
        }

        let userId = await profileImgUpload(response.userNo);
        if(userId === 0){
            $("#signup__button").attr("disabled",false);
            alert("회원가입 에러");
            return;
        }

        alert("로그인 페이지로 이동합니다.");
        //window.location.href = "login";
    }
}

function emailAdd(){

    const $emailID   = $("#signup__email").val();
    const $emailType = $("#signup__email-selectbox option:selected").val();

    if($emailID === ""){
        return alert("이메일 계정을 입력해주세요.");
    }else if($emailType === ""){
        return alert("이메일 종류를 선택해주세요.");
    }

    const $email     =  $emailID + "@" + $emailType;

    return $email;
}

// Semantic ui File
function fileOpen(e){
    $(e).parent().find("input:file").click();
}

function fileChange(e){
    try{
        var name = e.files[0].name;
        $('input:text', $(e).parent()).val(name);
    }catch(err){
        console.log(err)
    }
}

