$(document).ready(() => {
    myLibraryList();

    document.addEventListener('keydown', function(e){
          const keyCode = e.keyCode;

          if(keyCode == 13){ // Enter key
            searchEBook();
          }
        })

})

let listType = 1;

function isShared(itemId){
    $.ajax({
        type : "put",
        url : "/ebooks/shares/" + itemId,
        contentType : 'application/json',
        beforeSend:() =>{
            $("#loading__container").attr("style","display:inline-block;");
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

function goBook(){
    location.href = "book.html";
}

async function myLibraryList(){

    listType = 1;

    await $.ajax({
        type : "get",
        url : "/librarys",
        contentType : 'application/json',
        beforeSend:() =>{
            $("#loading__container").attr("style","display:inline-block;");
        },
        success : data => {
            myLibraryListData(data);
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


function searchEBook(){
    let searchTxt =  $('#search_txt').val()

    if(searchTxt === ""){
        myLibraryList();
        return;
    }

    $.ajax({
            type : "get",
            url : "/ebooks/searchs?name=" + searchTxt + "&type=" + listType,
            contentType : 'application/json',
            beforeSend:() =>{
                $("#loading__container").attr("style","display:inline-block;");
            },
            success : data => {
                if(listType == 1)
                    myLibraryListData(data);
                else
                    sharedBookListData(data);
            },
            error : json => {
                $("#loading__container").attr("style","display:none;");

                alert("다시 로그인 해주세요.");
                window.location.href = "/login"
            },
            complete:() =>{
                $("#loading__container").attr("style","display:none;");
            }
        })

}

function myLibraryListData(data) {
    let libraryList = data.libraryList
     $("#main__content-ul").html('')
     libraryList.forEach(item => {

        let itemId = item.ebookId;
        let ownerId = item.ebookUploader;
        let connectId = data.userId;

         $("#main__content-ul").append(
             `<li class='book-item' id=${item.ebookId}>
                 <img src=${item.ebookPosterPath} onclick=showEbookInfo(${item.ebookId}) class='book-shadow' />
                 <div class='book-title'>${item.ebookTitle}</div>
                 <div class='book-author'>${item.ebookAuthor}</div>
             </li>`
         );

         $("#"+itemId).append(
            (connectId === ownerId ?
                (item.isShared ?
                `<label class="switch">
                    <input type="checkbox" onclick=isShared(${itemId}) checked>
                   <span class="slider round"></span>
                 </label>`
                 : `<label class="switch" >
                       <input type="checkbox" onclick=isShared(${itemId})>
                        <span class="slider round"></span>
                    </label>`
                 )
                 :
                `<div class='ui animated button book-action-btn' onclick=libraryRemove(${item.ebookId})>
                   <div class="visible content">Remove</div>
                    <div class="hidden content">
                        <i class="trash alternate icon"></i>
                    </div>
                </div>`
            )
         );
     })
}

 async function sharedBookList(){

    listType = 2;

    await $.ajax({
        type : "get",
        url : "/ebooks/shares",
        contentType : 'application/json',
        beforeSend:() =>{
            $("#loading__container").attr("style","display:inline-block;");
        },
        success : data => {
            sharedBookListData(data);
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

  function sharedBookListData(data){
     $("#main__content-ul").html('')
     data.forEach(item => {
         $("#main__content-ul").append(
             `<li class='book-item'>
                 <img src=${item.ebookPosterPath} onclick=showEbookInfo(${item.ebookId}) class='book-shadow' />
                 <div class='book-title'>${item.ebookTitle}</div>
                 <div class='book-author'>${item.ebookAuthor}</div>
                 <div class='ui animated button book-action-btn' onclick=libraryAdd(${item.ebookId})>
                     <div class='visible content'>Add</div>
                     <div class='hidden content'>
                         <i class='plus icon'></i>
                     </div>
                 </div>
             </li>`
         );
     })
  }

 async function showEbookInfo(ebookId){

     await $.ajax({
        type : "get",
        url : `/ebooks/${ebookId}`,
        contentType : 'application/json',
        beforeSend:() =>{
            $("#loading__container").attr("style","display:inline-block;");
        },
        success : data => {
            EBookinfoData(data);
             $('#book__info-dialog').modal('show');
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
 function EBookinfoData(data){
    $("#book__info-poster").attr("src",data.ebook.ebookPosterPath)
    $("#book__info-title").html(data.ebook.ebookTitle)
    $("#book__info-subtitle").html(data.ebook.ebookSubTitle)
    $("#book__info-author").html(data.ebook.ebookAuthor)
    $("#book__info-category").html(data.category)
    $("#book__info-uploader").html(data.ebook.ebookUploader)
    $("#book__info-created").html(formatDate(data.ebook.ebookCreated))
 }

 function formatDate(date) {
     var d = new Date(date),
         month = '' + (d.getMonth() + 1),
         day = '' + d.getDate(),
         year = d.getFullYear();

     if (month.length < 2)
         month = '0' + month;
     if (day.length < 2)
         day = '0' + day;

     return [year, month, day].join('-');
 }

 function libraryAdd(ebookId){

    let data = {
        ebookId : ebookId
    }
    $.ajax({
        type : "post",
        url : `http://localhost:8080/librarys`,
        contentType : 'application/json',
        data : JSON.stringify(data),
         beforeSend:() =>{
            $("#loading__container").attr("style","display:inline-block;");
        },
        success : data => {
            alert(data.message);
        },
        error : json => {
            $("#loading__container").attr("style","display:none;");
            let errorMsg = json.responseJSON.message;
            alert(errorMsg);
        },
        complete:() =>{
            $("#loading__container").attr("style","display:none;");
        }
    })
 }


 function libraryRemove(ebookId){

     $.ajax({
         type : "delete",
         url : `http://localhost:8080/librarys/${ebookId}`,
         contentType : 'application/json',
          beforeSend:() =>{
             $("#loading__container").attr("style","display:inline-block;");
         },
         success : data => {
             myLibraryList();
             alert(data.message);
         },
         error : json => {
             $("#loading__container").attr("style","display:none;");
             let errorMsg = json.responseJSON.message;
             alert(errorMsg);
         },
         complete:() =>{
             $("#loading__container").attr("style","display:none;");
         }
     })
 }

 function getCategoryList(){

    console.log("a");
 }



