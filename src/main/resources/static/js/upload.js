$(document).ready(() => {
    $('.ui.dropdown').dropdown()
    getCategoryList();

})


function getCategoryList(){
    $.ajax({
            type : "get",
            url : "/categorys",
            contentType: "application/json",
            beforeSend:()=>{
                $("#loading__container").attr("style","display:inline-block;");
            },
            success : data => {

                console.log(data);
                let categoryList = data.categoryList;

                categoryList.forEach((item) => {
                    $("#book_category").append("<option value='" + item.categoryId + "'>" + item.categoryName + "</option>");
                })
            },
            error : json => {
                $("#loading__container").attr("style","display:none;");
                let errorMsg = json.responseJSON.message;
                alert(errorMsg);
                window.location.href = "/login"
            },
            complete : () => {
                $("#loading__container").attr("style","display:none;");
            }
        })
}


function goMain(){
    location.href = "/";
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

async function eBookUpload(){

    let ebookID = 0;

    ebookID = await fileUpload();
    if(ebookID === 0){
        //alert("책 번호가 존재하지 않습니다.")
        return;
    }

    await eBookInfoUpload(ebookID);
}

async function fileUpload(){
    let fileInput = $(`#file-book`)[0].files[0];
    let posterInput = $(`#file-poster`)[0].files[0];
    let files = [fileInput, posterInput]

    if(files[0] === undefined){
            alert("책을 등록해주세요.");

        return 0;
    }

    let formData = new FormData();
    formData.append("files",fileInput);
    formData.append("files",posterInput);

    let ebookID = 0;

    await $.ajax({
        type : "post",
        url : "/ebooks/uploads/files",
        contentType: false,
        processData:false,
        data : formData,
        beforeSend:()=>{
            $("#loading__container").attr("style","display:inline-block;");
            $("#upload__button").attr("disabled",true);
        },
        success : data => {
            ebookID = data.ebook.ebookId;
        },
        error : json => {
            $("#loading__container").attr("style","display:none;");
            let errorMsg = json.responseJSON.message;
            alert(errorMsg);
            window.location.href = "/login"
        }
    })

    return ebookID;
}

async function eBookInfoUpload(ebookID){

    let rdoId = $('input[name="rdo"]:checked').attr("id");

    if(rdoId === undefined){
        alert("공유값이 존재하지 않습니다."); return;
    }

    let isShared;

    if(rdoId === "yes")
        isShared = true;
    else
        isShared = false;

    let ebook = {
        ebookId         : ebookID,
        ebookTitle      : $("#book_title").val(),
        ebookSubTitle   : $("#book_subtitle").val(),
        categoryId         : $("#book_category").val(),
        ebookAuthor     : $("#book_author").val(),
        isShared        : isShared
    }

    await $.ajax({
        type : "post",
        url : "/ebooks/uploads/infos",
        contentType: 'application/json',
        dataType: 'json',
        data : JSON.stringify(ebook),
        success : data => {
            alert("책이 업로드 되었습니다.");
            window.location.href = "/"
        },
        error : json => {
            let errorMsg = json.responseJSON.message;
            alert(errorMsg);
            $("#upload__button").attr("disabled",false);
        },
        complete:()=>{
            $("#loading__container").attr("style","display:none;");
        }
    })
}


