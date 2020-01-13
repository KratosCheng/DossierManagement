var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(header, token)
});

$(document).on("change", "#his-search-form", function () {
    hisSubmit();
});

function changeHisPage(pageNum) {
    $("#hisPageNumInput").val(pageNum);
    hisSubmit();
}

function hisSubmit() {
    var formData = new FormData($("#his-search-form")[0]);

    $.ajax({
        //几个参数需要注意一下
        type: "post",//方法类型
        // dataType: "json", //预期服务器返回的数据类型
        url: "../user/hisRefresh", //url
        data: formData,
        processData: false,			//对数据不做处理
        contentType: false,
        success: function (result) {
            console.log(result);
            $('#his-list-container').html(result);
        }
    })
}

function hisReset() {
    $.ajax({
        //几个参数需要注意一下
        type: "get",//方法类型
        url: "../user/hisReset", //url
        success: function (result) {
            console.log(result);
            $('#his-list-container').html(result)
        }
    })
}

$(document).on("change", "#account-search-form", function () {
    accountSubmit();
});

function changeAccountsPage(pageNum) {
    $("#accountPageNumInput").val(pageNum);
    accountSubmit();
}

function accountSubmit() {
    var formData = new FormData($("#account-search-form")[0]);

    $.ajax({
        type: "post",
        url: "../user/accountRefresh",
        data: formData,
        processData: false,
        contentType: false,
        success: function (result) {
            console.log(result);
            $('#account-list-container').html(result);
        }
    })
}

function accountReset() {
    $.ajax({
        type: "get",
        url: "../user/accountReset",
        success: function (result) {
            console.log(result);
            $('#account-list-container').html(result)
        }
    })
}

function showAccountList() {
    $("#user-his-div").hide();
    $("#admin-user-list-div").show();
    $("#admin-operation-list-div").hide();
}

function showHisList() {
    $("#user-his-div").show();
    $("#admin-user-list-div").hide();
    $("#admin-operation-list-div").hide();
}

function showAccountHisList() {
    $("#admin-operation-list-div").show();
    $("#user-his-div").hide();
    $("#admin-user-list-div").hide();
}

function showModifyDialog(modifyUser) {
    $("#modifyJobNum").val(modifyUser.jobNum);
    document.getElementById("modifyJobNum").setAttribute("readonly", "true");
    $("#modifyPassword").val(modifyUser.password);
    $("#modifyRoleName").val(modifyUser.roleName);
    $("#modifyCreator").val(modifyUser.creator);
    $("#modifyCreateTime").val(modifyUser.createTime);
    $("#modify_user_dialog").modal('show');
    $(".form-hint").show();
}

function modifyUser() {
    var formData = new FormData($("#modify-user-form")[0]);

    $.ajax({
        type: "post",
        url: "../admin/updateUser",
        data: formData,
        processData: false,			//对数据不做处理
        // cache:false,      				//上传文件不需要缓存
        contentType: false,
        mimeType:"multipart/form-data",
        success: function (result) {
            result = JSON.parse(result);
            if (result.code === 0) {
                alert("用户信息编辑成功！");
                $("#modify_user_dialog").modal('hide');
                accountReset();     // 刷新用户表
            } else {
                alert(result.code + result);
            }
        },
        error: function (r) {
            alert("error" + r.msg);
        }
    })
}

function showAddUserDialog() {
    $("#modify-user-form")[0].reset();
    document.getElementById("modifyJobNum").removeAttribute("readonly");
    $("#modify_user_dialog").modal('show')
    $(".form-hint").hide();
}

function deleteUser(jobNum) {
    if (confirm("操作不可逆，确认删除用户（" + jobNum + "）?")) {
        $.ajax({
            type: "get",
            url: "../admin/removeUser/" + jobNum,
            success: function (result) {
                result = JSON.parse(result);
                if (result.code === 0) {
                    alert("用户信息编辑成功！");
                    $("#modify_user_dialog").modal('hide');
                    accountReset();     // 刷新用户表
                } else {
                    alert(result.code + result);
                }
            },
            error: function (r) {
                alert("error" + r.msg);
            }
        })
    }
}