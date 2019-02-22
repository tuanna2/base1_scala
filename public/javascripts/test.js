$(document).ready(function(){
    var page = parseInt(window.location.href.split("/").slice(length-1))
    var countPage = Math.ceil($('#length').val()/5);

    $('#pagination').append('<li class="paginate_button"><a href="/student/page/'+1+'">First</a></li>');
    page==1?
        $('#pagination').append('<li class="paginate_button previous disabled"><a>Previous</a></li>')
        :
        $('#pagination').append('<li class="paginate_button previous"><a href="/student/page/'+(page-1)+'">Previous</a></li>');

    if(page > 3)
        $('#pagination').append('<li class="paginate_button"><a>...</a></li>')

    for(var lap=1,j = page > 3?(page-2):1;j<countPage;lap++,j++){
        j==page?
            $('#pagination').append('<li class="paginate_button active"><a>'+j+'</a></li>')
            :
            $('#pagination').append('<li class="paginate_button"><a href="/student/page/'+j+'">'+j+'</a></li>');
        if(lap>=5) break;
    }

    if(page < countPage -3)
        $('#pagination').append('<li class="paginate_button"><a>...</a></li>')

    page==countPage || $('#length').val()==0?
        $('#pagination').append('<li class="paginate_button next disabled"><a>Next</a></li>')
        :
        $('#pagination').append('<li class="paginate_button next"><a href="/student/page/'+(page+1)+'">Next</a></li>');
    $('#pagination').append('<li class="paginate_button"><a href="/student/page/'+countPage+'">Last</a></li>');


})