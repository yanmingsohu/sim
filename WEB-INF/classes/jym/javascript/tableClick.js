/**
 * 表格行鼠标点击事件
 * 
 * @param tableid - 要监听的表格id
 * @param func - 监听函数,第一个参数为当前鼠标悬停的tr对象,
 * 						第二个参数是当前行的索引从0开始
 * @return 返回tableid的对象
 */
function tableRowMouseOverListener(tableid, func) {
	var table = document.getElementById(tableid);

	if (table==null) return;
	var rows = table.rows;
	for (var i=0; i<rows.length; ++i) {
		(function () {
			var funcstr = func;
			var row = rows[i];
			var rowindex = i;
			
			row.onclick = function () {
				funcstr(row, rowindex);
			}
		})();
	}
	
	return table;
}