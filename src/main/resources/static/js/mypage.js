$(document).ready(() => {
    myLibraryInfo();
})

async function myLibraryInfo(){

    await $.ajax({
        type : "get",
        url : "/mypages",
        contentType : 'application/json',
        beforeSend:() =>{
            $("#loading__container").attr("style","display:inline-block;");
        },
        success : data => {

            $("#myLibraryBookCnt").html(data.libraryBooks);
            $("#myUploadBookCnt").html(data.uploadBooks);
            $("#myName").html(data.name)
            $("#myEmail").html(data.email)
            $("#myProfileImg").attr("src","/mypages/images?no="+data.profileImgNo);
        },
        error : json => {
            $("#loading__container").attr("style","display:none;");
            let errorMsg = json.responseJSON.message;
            alert(errorMsg);
            window.location.href = "/login"
        },
        complete:() =>{
            $("#loading__container").attr("style","display:none;");
        }
    })
}

async function passwordChange(){

    var name_value = prompt("변경할 비밀번호를 입력해주세요.", "");

    const $userData = {
        password : name_value
    }

    if(name_value) {

        await $.ajax({
            type : "put",
            url : "/mypages/passwords",
            data : $userData,
            contentType : 'application/json',
            beforeSend:() =>{
                $("#loading__container").attr("style","display:inline-block;");
            },
            success : data => {
                if(data === true)
                    alert("변경되었습니다.")
                else
                    alert(data)
            },
            error : json => {
                $("#loading__container").attr("style","display:none;");
                let errorMsg = json.responseJSON.message;
                alert(errorMsg);
                window.location.href = "/login"
            },
            complete:() =>{
                $("#loading__container").attr("style","display:none;");
            }
        })
    }
}
