function load(data){
        var dayOfWeek = [
            "日", "月", "火", "水", "木", "金", "土"
        ]
        var names = [];
        var tasks = [];
        var startDate = new Date(data[0].from).getTime();
        var endDate = new Date(data[0].from).getTime();


        for (var i = 0; i < data.length; i++) {
            if (startDate > new Date(data[i].from).getTime())
                startDate = new Date(data[i].from).getTime();
            if (endDate < new Date(data[i].to).getTime())
                endDate = new Date(data[i].to).getTime();

            names.push(data[i].name)
            tasks.push({
                id: i,
                code:data[i].code,
                name:data[i].name
            });
        }

        if (endDate - startDate < 30*24*3600*1000) {
            endDate = startDate + 30*24*3600*1000;
        }
        var countDate = 0;
        var currentMonth = new Date(startDate).getMonth() + 1;

        var table1 = new Table();
        $("#freeze1").append(table1.getView());
        var row1_1 = new TableRow();
        row1_1.addCell(new TableCell({content: "<strong>工事項目<br/>番号</strong>", class:"my-cell my-cell-fixed"}));
        row1_1.addCell(new TableCell({content: "<strong>工事項目名</strong>", class:"my-cell my-cell-fixed"}));
        table1.addRowToHead(row1_1);

        var table2 = new Table();
        $("#freeze2").append(table2.getView());

        var row2_1 = new TableRow();
        var row2_2 = new TableRow();

        table2.addRowToHead(row2_1);
        table2.addRowToHead(row2_2);

        var countDate = 0;
        var currentMonth = new Date(startDate).getMonth() + 1;

        //header
        for(var i = startDate; i <= endDate; i+= 86400 * 1000){
            var d = new Date(i);

            var strTime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate();
            var detailLink = window.location.href.replace('item', 'nippo').replace('gantt', strTime);

            var classDay = "";
            if(d.getDay() == 0){
                classDay = "sunday";
            }
            if(d.getDay() == 6){
                classDay = "saturday";
            }
            var cell = new TableCell({class:"my-cell text-center " + classDay,content: "<a href=\"" + detailLink + "\" class='page'><strong>" + d.getDate() + "日<br/>" + dayOfWeek[d.getDay()] + "</strong></a>"});
            row2_2.addCell(cell);
            if(currentMonth !== (d.getMonth()+1) || i == endDate){
                if(i == endDate){
                    countDate++;
                }
                row2_1.addCell(new TableCell({content: "<strong>" + currentMonth + "月</strong>", colSpan: countDate}));
                currentMonth = d.getMonth()+1;
                countDate = 1;
            }else{
                countDate++;
            }
        }

        var table4 = new Table();
        $("#freeze4").append(table4.getView());
        //data
        for(var t = 0; t < tasks.length; t++){
            var row = new TableRow({id:"row_task_"+tasks[t].id});
            table4.addRowToBody(row);
            var j = 0;
            for(var i = startDate; i <= endDate; i+= 86400 * 1000){
                var cellId = "task_" + tasks[t].id + "_" + i;
                var cellId = "task_" + tasks[t].id + "_" + j;
                j++;
                row.addCell(new TableCell({id: cellId, content: " ", class:"my-cell text-center"}));
            }
        }

        var table3 = new Table();
        $("#freeze3").append(table3.getView());
        //data
        for(var t = 0; t < tasks.length; t++){
            var row = new TableRow({id:"row_task_"+tasks[t].id});
            table3.addRowToBody(row);
            row.addCell(new TableCell({content: tasks[t].code, class:"my-cell"}));
            row.addCell(new TableCell({content: tasks[t].name, class:"my-cell"}));
        }

        // sample content
        for (var i = 0; i < data.length; i++) {
            var from = getIndex(startDate, new Date(data[i].from).getTime());
            var to = getIndex(startDate, new Date(data[i].to).getTime());
            console.log(from + "---" + to);
            checkGantt(i, from, to);
        }

        applyFreeze("#freeze1", "#freeze2", "#freeze3", "#freeze4");
        var scrollBarWidth = getScrollbarWidth();

        $("#freeze2").css("maxWidth", $("#table2").width() - 140);
        $("#freeze4").css("maxWidth", $("#table2").width() - 140);

        $(".hide-scroll").each((index, element) => {
            $(element).parents(".wrapper")
                .width($(element).width()-scrollBarWidth)
                .height($(element).height()-scrollBarWidth)
                .css("overflow", "hidden");
            $(element).width($(element).width() + scrollBarWidth);
        });
}

function checkGantt(index, from, to) {
    for (var i = from; i <= to; i++)
        $("#task_"+index+"_"+i).html(this.getCellContentDemo(1, 0));
}

function getIndex(base, date) {
    return Math.floor((date - base) / (24 * 3600 * 1000));
}

function getCellContentDemo(numberPartUp, numberPartDown, textUp){

    if (typeof textUp === "undefined") textUp = "";

    var cellContent = $("<div style='width: 100%; height: 100%;'></div>");
    var div1 = $("<div style='height: 50%; width: 100%;'></div>");
    cellContent.append(div1);
    var div2 = $("<div style='height: 50%; width: 100%;'></div>");
    cellContent.append(div2);

    if(numberPartUp){
        var w1 = 100 / numberPartUp;
        for(var i = 0; i < numberPartUp; i++){
            div1.append("<div style='width: "+w1+"%; height: 100%; background-color: #d8d8d8; border: 1px solid #d8d8d8; float: left'>"+textUp+"</div>");
        }
    }

    if(numberPartDown){
        var w2 = 100 / numberPartDown;
        for(var j = 0; j < numberPartDown; j++){
            div2.append("<div style='width: "+w2+"%; height: 100%; background-color: gray; border: 1px solid #d8d8d8; float: left'></div>");
        }
    }
    return cellContent;
}

$.get(window.location.pathname.replace("gantt", "json"), function(data) {
    load(JSON.parse(data));
})
