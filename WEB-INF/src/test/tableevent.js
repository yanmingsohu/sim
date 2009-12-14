
function editTableData(row, rowindex) {
	if (rowindex==0) return;
	
	alert(rowindex);
}

/**
 * 移动div指定的层到屏幕的中央
 * 
 * @param div - div对象
 */
function moveCenter(div) {
	var w = div.clientWidth;
	var h = div.clientWidth;
	var x = (document.body.clientWidth - w)/2
	var y = (document.body.clientHeight - h)/2
	div.style.top = y;
	div.style.left = x;
	
	return div;
}