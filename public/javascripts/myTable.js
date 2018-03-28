class Table {
	constructor(opts){
		opts = opts || {};
		this.id = opts.id || Math.random();
		this.class = opts.class || '';
		this.instance = this.build();
	}

	addRowToHead(row){
		this.instance.find("thead").append(row.getView());
	}

	addRowToBody(row){
		this.instance.find("tbody").append(row.getView());
	}

	build(){
		return $("<table border=1 id='"+this.id+"' class='"+this.class+"'><thead></thead><tbody></tbody></table>");
	}

	getView(){
		return this.instance;
	}
}

class TableRow {
	constructor(opts){
		opts = opts || {};
		this.id = opts.id || Math.random();
		this.class = opts.class || "";
		this.instance = this.build();
	}

	addCell(cell){
		this.instance.append(cell.getView());
	}

	build(){
		return $("<tr row id='"+this.id+"' class='"+this.class+"'></tr>");
	}

	getView(){
		return this.instance;
	}
}

class TableCell {
	constructor(opts){
		opts = opts || {};
		this.id = opts.id || Math.random();
		this.class = opts.class || "";
		this.rowSpan = opts.rowSpan || 1;
		this.colSpan = opts.colSpan || 1;
		this.content = opts.content || "default";
		this.style = opts.style || "";
		this.instance = this.build();
	}

	build(){
		return $("<td id='"+this.id+"' class='"+this.class+"' rowspan='"+this.rowSpan+"' colspan='"+this.colSpan+"' style='"+this.style+"'>"+this.content+"</td>");
	}

	getView(){
		return this.instance;
	}
}