$(document).ready(function() {
 //href="@controllers.pages.routes.TermController.delete(term.get.code)"

    $("#btnTermDelete").click(function(e) {
        var arr = window.location.href.split('/');
        var code = arr[arr.length-1];
        var r = confirm("本当に"+code+"を削除しますか？");
        if (r) {
            $.get('/term/delete/'+code, function(data) {
                window.location = '/term';
            })
        }
    })

    $("#btnConstructionDelete").click(function(e) {
        var arr = window.location.href.split('/');
        var code = arr[arr.length-1];
        var r = confirm("本当に\""+code+"\"を削除しますか？");
        if (r) {
            $.get('/construction/delete/'+code, function(data) {
                window.location = '/construction';
            })
        }
    })

    $('#from_picker').datetimepicker({
        format: 'YYYY年MM月DD日',
        locale: 'ja'
    });

    $('#to_picker').datetimepicker({
        format: 'YYYY年MM月DD日',
        locale: 'ja'
    });

    $("#btnStepDelete").click(function(e) {
        var arr = window.location.href.split('/');
        var code = arr[arr.length-1];
        var r = confirm("本当に\""+code+"\"を削除しますか？");
        if (r) {
            $.get('/step/delete/'+code, function(data) {
                window.location = '/construction';
            })
        }
    })




    $("#btnUserDelete").click(function(e) {
        var arr = window.location.href.split('/');
        var code = arr[arr.length-1];
        var r = confirm("本当に\""+code+"\"を削除しますか？");
        if (r) {
            $.get('/users/delete/'+code, function(data) {
                window.location = '/users';
            })
        }
    })

    $('#pagingTable').paginate({
        limit: 5,
        first: false,
        last: false,
        nextText: "»",
        previousText: "«",
        navigationWrapper: $('nav#pageArea')
    });

})
