/**
 * 修改tableid指定的表的颜色，其颜色为隔行显示
 * 
 * @param tableid - 表格的id
 * @param fcolor - 奇数行的颜色
 * @param scolor - 偶数行的颜色
 * @param mousecolor - 鼠标悬停的颜色
 * @return 返回tableid的对象
 */
function changeTableColor(tableid, fcolor, scolor, mousecolor) {
	var table = getByid(tableid);
	if (table==null) return;
	
	var color = true;
	
	if (fcolor==null) {
		fcolor = '#ddd';
	}
	if (scolor==null) {
		scolor = '#f0f0f0';
	}
	if (mousecolor==null) {
		mousecolor = '#faa';
	}
	
	var rows = table.rows;
	for (var i=0; i<rows.length; ++i) {
		var row = rows[i];
		
		if (color) {
			changeColor(row, fcolor);
		} else {
			changeColor(row, scolor);
		}
		color = !color;

		onMouseOverChangeColor(row, mousecolor);
	}
	
	return table;
}

/**
 * 当鼠标悬停在obj标记上时，颜色变为color
 * 
 * @param obj - html标记对象
 * @param color - 有效的css颜色值
 * @return null
 */
function onMouseOverChangeColor(obj, color) {
	var oldcolor = obj.style.backgroundColor;
	var r = obj;
	var ncolor = color;
	
	r.onmouseover = function() {
		changeColor(r, ncolor);
	}
	r.onmouseout = function() {
		changeColor(r, oldcolor);
	}
}

/**
 * 修改obj的颜色为color
 * 
 * @param obj - html标记对象
 * @param color - 有效的css颜色值
 * @return null
 */
function changeColor(obj, color) {
	obj.style.backgroundColor = color;
}

function getByid(id) {
	return document.getElementById(id);
}