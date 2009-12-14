
function editTableData(row, rowindex) {
	if (rowindex==0) return;
	
	var div = document.getElementById('editdialog');
	div.style.display = "block";
	moveCenter(div);
	var forms = div.getElementsByTagName('input');
	var tds = row.getElementsByTagName("td");
	
	var func = function (name, findex, tdindex) {
		if (forms[findex].name==name) {
			forms[findex].value = tds[tdindex].innerText;
		}
	}
	
	for (var i=0; i<forms.length; ++i) {
		var form = forms[i];
		func('bookname', i, 1);
		func('price', i, 2);
		func('author', i, 3);
		func('id', i, 0);
	}
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