/* CSS 样式选择控件
 * 依赖common.js */
(function() {
	
/** create tag */
function cc(name, _parent) {
	var _tag = document.createElement(name);
	if (_parent) _parent.appendChild(_tag);
	return _tag;
}

function chref(text, _parent) {
	var href = cc('a', _parent);
	href.innerHTML = text;
	href.href = '#';
	href.style.marginLeft = '5px';
	return href;
}

var _ie = navigator.appName.search('Microsoft')>=0;

var css_color_list = {
	'黑色' : '#000000',	'白色' : '#FFFFFF', '鸨色' : '#f7acbc',
	'曙色' : '#bd6758', '红色' : '#d71345', '梅染' : '#987165',
	'朱色' : '#f26522', '绯色' : '#aa2116', '焦香' : '#853f04',
	'栗梅' : '#6b2c25', '金赤' : '#f15a22', '桦色' : '#c85d44',
	'黄茶' : '#de773f', '橙色' : '#f47920', '茶色' : '#8f4b2e',
	'杏色' : '#fab27b', '黄栌' : '#64492b', '香色' : '#8e7437',
	'土色' : '#b36d41', '金色' : '#c37e00', '山吹' : '#fcaf17',
	'砂色' : '#d3c6a6', '芥子' : '#c7a252', '淡黄' : '#dec674',
	'油色' : '#817936', '媚茶' : '#454926', '黄緑' : '#b2d235',
	'苔色' : '#5c7a29', '萌黄' : '#a3cf62', '柳色' : '#78a355',
	'白緑' : '#cde6c7', '青緑' : '#007d65', '萌葱' : '#006c54',
	'锖鼠' : '#122e29', '白群' : '#78cdd1', '新桥' : '#50b7c1',
	'瓮覗' : '#94d6da', '露草' : '#33a3dc', '浓蓝' : '#11264f',
	'花色' : '#2b4490', '瑠璃' : '#2a5caa', '杜若' : '#426ab3',
	'胜色' : '#46485f', '铁绀' : '#181d4b', '褐返' : '#0c212b',
	'青褐' : '#121a2a', '藤纳' : '#6a6da9', '藤紫' : '#9b95c9',
	'菫色' : '#6f60aa', '鸠羽' : '#867892', '薄色' : '#918597',
	'银鼠' : '#a1a3a6', '铅色' : '#72777b', '黒铁' : '#281f1d',
	'江戸紫' : '#6f599c', '葡萄鼠' : '#63434f', '茄子绀' : '#45224a',
	'菖蒲色' : '#c77eb5', '牡丹色' : '#ea66a6', '石竹色' : '#d1c7b7',
	'象牙色' : '#f2eada', '薄墨色' : '#74787c', '煤竹色' : '#6c4c49',
	'无色' : ''
};

var css_border_style = {
	'无边框'	: 'none',
	'虚线'	: 'dotted',
	'实线'	: 'solid',
	'双实线'	: 'double',
	'3D凹槽'	: 'groove',
	'菱形'	: 'ridge',
	'3D凹边'	: 'inset',
	'3D凸边'	: 'outset'
};

var css_align_type = {
	'左对齐'		: 'left',
	'右对齐'		: 'right',
	'居中'		: 'center',
	'两端对齐'	: 'justify'
};

var css_font_weight = {
	'正常'		: 'normal',
	'细体'		: 'lighter',
	'粗体'		: 'bold',
	'特粗体'		: 'bolder'
}

var css_shadow_type = {
//	name : [css3, ie]
	 '无阴影'		: (!_ie ? '' 					: 'Strength=0, Direction=  0, Color=#888888')
	,'下45度硬边'	: (!_ie ? ' 3px  3px #888' 		: 'Strength=8, Direction=135, Color=#888888')
	,'下45度模糊'	: (!_ie ? ' 3px  3px 5px #888'	: 'Strength=4, Direction=135, color=#888888')
	,'上45度硬边'	: (!_ie ? '-3px -3px #888'		: 'Strength=8, Direction=-45, Color=#888888')
	,'上45度模糊'	: (!_ie ? '-3px -3px 5px #888'	: 'Strength=4, Direction=-45, Color=#888888')
	,'四边轻'		: (!_ie ? ' 0 0  5px #888'		: 'Strength=4, Direction=180, Color=#888888')
	,'四边重'		: (!_ie ? ' 0 0  5px 5px #888'	: 'Strength=8, Direction=180, Color=#888888')
};

var css_dialog_struct = [
/*	[name, css, type, {name:value,...}]
 *  type::	's' -> select
 *  		'c' -> color select
 *  		'n' -> number */
	 [null]
	,['背景色',		'backgroundColor',	'c', css_color_list			]
	,['字体色',		'color', 			'c', css_color_list			]
	,['字体大小',	'fontSize',			'n', {'min': 5, 'max':40}	]
	,['字间距',		'letterSpacing',	'n', {'min': 0, 'max':20}	]
	,['字粗细',		'fontWeight',		's', css_font_weight		]
	,['对齐方式',	'textAlign',		's', css_align_type			]
	,[null]
	,['透明度',		'opacity',			'n', {'min': 1, 'max':100}	]
	,['阴影样式',	'boxShadow',		's', css_shadow_type		]
	,[null]
	,['边框样式',	'borderStyle',		's', css_border_style		]
	,['边框宽度',	'borderWidth',		'n', {'min':0 , 'max':50}	]
	,['边框颜色', 	'borderColor',		'c', css_color_list			]
	,[null]
	,['宽度',		'width',			'n', {'min':10, 'max':500}	]
	,['高度',		'height',			'n', {'min':10, 'max':500}	]
	,['内边距',		'padding',			'n', {'min':0 , 'max':50}	]
	,['外边距',		'margin',			'n', {'min':0 , 'max':50}	]
	,[null]
];

var css_default_values = {
	 'backgroundColor'	: '#FFFFFF'
	,'color'			: '#000000'
	,'fontSize'			: '12'
	,'letterSpacing'	: '0'
	,'borderStyle'		: 'none'
	,'borderWidth'		: '1'
	,'borderColor'		: '#FFFFFF'
	,'padding'			: '0'
	,'margin'			: '0'
	,'width'			: ''
	,'height'			: ''
	,'textAlign'		: 'left'
	,'opacity'			: ''
	,'boxShadow'		: ''
}

var CSS_DEFAULT_VAL = '';

/**
 * 创建对话框的方法,返回的对象不可以复用
 * 
 * @param {} _parent - 该对话框的父对象容器
 * @param {} _initValue - 初始化参数,如果无效使用内部定义的初始化值,当'确定'按钮
 * 						被点击后,该当前样式参数将被复制到该map中
 * @param [] _objArrs - 存储所有控制样式对象的数组
 */
function createCssDialog(_parent, _initValue, _objArrs) {
	
	if (!_objArrs) throw new Error("参数3:被控对象数组不能为空");
	if (!_initValue) throw new Error("参数2:初始化数组不能为空");
		
	/** 创建参数的副本 */
	var modifyValue = {};
	
	for (var i in css_dialog_struct) {
		if (css_dialog_struct[i] && css_dialog_struct[i][1]) {
			var name = css_dialog_struct[i][1];
			if ( typeof _initValue[name] != 'string' ) {
				_initValue[name] = css_default_values[name];	
			}
			modifyValue[name] = _initValue[name];
		}
	}
		
	/** 用来保存释放内存的过程 */
	var _frees = [];
	var _width = 300;
	/** 事件监听器 */
	var _listeners = [];
	var closed = false;
	
	/** 创建主框架 */
	var rh = create_round_horn(_parent);
	rh.autoMove(false);
	rh.frame.style.width = _width + 'px';
	
	/** 使框架可以移动 */
	var move = new DivPack(rh.frame, rh.frame);
	
	/** 创建内容元素 */
	var table = cc('table', rh.content);
	table.style.width = (_width-30) + "px";
	table.style.border = '0';
	var tbody = cc('tbody', table);
	
	var tmp = css_dialog_struct.length;
	for (var ri = 0; ri<tmp; ++ri) {
		if (!css_dialog_struct[ri][0]) {
			createSeptation();
			continue;
		}
		
		(function () {
			var type = css_dialog_struct[ri][2];
			var tag = null;
			var _ri = ri;
			
			if (type=='c') {
				tag = createColorRow(css_dialog_struct[ri]);
			} 
			else if (type=='n') {
				tag = createNumRow(css_dialog_struct[ri]);
			} 
			else {
				tag = createSelectRow(css_dialog_struct[ri]);
			}
	
			_freeListener( onchange(tag, function() {
				changeStyle(css_dialog_struct[_ri][1], tag.value, tag);
			}) );
		})();
	}
	
	
	/* 创建按钮 */
	var tr = cc('tr', tbody);
	var td = cc('td', tr); // 占位
	var td = cc('td', tr);
	
	var ok = chref('确定', td);
	ok.onclick = function() {
		for (var i in modifyValue) {
			_initValue[i] = modifyValue[i]; 
		}
		_sendEvent('ok');
		_close();
	}
	
	var cancel = chref('取消', td);
	cancel.onclick = function() {
		_sendEvent('cancel');
		// 后退所有修改
		for (var i in _initValue) { 
			changeStyle(i, _initValue[i]);
		}
		_close();
	}
	
	_freeListener(function() {
		cancel.onclick = null;
		ok.onclick = null;
	});
	
	
	function createSeptation() {
		var tr = cc('tr', tbody);
		var td = cc('td', tr);
		td.colSpan = 2;
		var div = cc('div', td);
		
		div.style.height = '2px';
		div.style.width = '100%';
		div.style.overflow = 'hidden';
		div.style.backgroundColor = '#cccccc';
		div.style.margin = '5px';
	}
	
	function createColorRow(_arr) {
		var select = createSelect(_arr[0]);
		var color_list = _arr[3];
		
		for (var cl in color_list) {
			var option = cc('option', select);
			option.value = color_list[cl];
			option.style.backgroundColor = color_list[cl];
			
			if (cl!='白色' && cl!='无色') {// 低效 
				option.style.color = '#FFFFFF';
			}
			option.innerHTML = cl;

			if (_initValue[_arr[1]]==color_list[cl]) {
				option.selected = true;
			}
		}
		return select;
	}
	
	function createSelectRow(_arr) {
		var select = createSelect(_arr[0]);
		var op_list = _arr[3];
		
		for (var cl in op_list) {
			var option = cc('option', select);
			option.value = op_list[cl];
			option.innerHTML = cl;
			
			if (_initValue[_arr[1]]==op_list[cl]) {
				option.selected = true;
			}
		}
		return select;
	}
	
	function createNumRow(_arr) {
		var td = createTSc(_arr[0]);
		var input = cc('input', td);
		var add = chref('+1', td);
		var sub = chref('-1', td);
		
		if (_initValue[_arr[1]]) {
			input.value = _initValue[_arr[1]];
		}
		input.size = 5;
		
		add.onclick = function() {
			var i = parseInt(input.value) + 1;
			if (i<=_arr[3].max) {
				input.value = i;
			} else {
				input.value = 0;
			}
		}
		sub.onclick = function() {
			var i = parseInt(input.value) - 1;
			if (i>=_arr[3].min) {
				input.value = i;
			} else {
				input.value = 0;
			}
		}
		
		_freeListener(function() {
			sub.onclick = null;
			add.onclick = null;
		});
		
		return input;
	}
	
	function createSelect(_title_name) {
		var td = createTSc(_title_name);
		var select = cc('select', td);
		select.style.width = '100px';
		return select;
	}
	
	function createTSc(_title_name) {
		var tr = cc('tr', tbody);
		var th = cc('th', tr);
		th.innerHTML = _title_name;
		th.style.width = '40%';
		var td = cc('td', tr);
		return td;
	}
	
	/* 如果_name属性不能使用_value赋值,_input会被清空 */
	function changeStyle(_name, _value, _input) {
		for (var i in _objArrs) {
			if (i!='length' && _objArrs[i] && _objArrs[i].style) {
				try {
					_changeStyle(_objArrs[i], _name, _value);
				} catch (e) {
					_input && (_input.value = CSS_DEFAULT_VAL);
					_value = CSS_DEFAULT_VAL;
					break;
				}
			}
		}
		modifyValue[_name] = _value;
	}
	
	function _freeListener(_o) {
		_frees.push(_o);
	}
	
	/** 插入新的标签对象并用当前样式属性设置他 */
	function _addTag(_tag) {
		_objArrs.push(_tag);
		for (var name in modifyValue) {
			_tag.style[name] = modifyValue[name];
		}
	}
	
	function _show(_x, _y) {
		if (closed) return;
		_sendEvent('show');
		if (!_x) _x = (document.body.clientWidth - 300)/2;
		if (!_y) _y = 150;
		move.setX(_x);
		move.setY(_y);
		rh.show(_x, _y);
	}
	
	function _close() {
		if (closed) return;
		closed = true;
		
		_sendEvent('close');
		rh.close();
		move.free();
		for (var i=_frees.length-1; i>=0; --i) {
			if ( _frees[i] ) {
				_frees[i]();
				_frees[i] = null;
			}
		}
		_frees = null;
		_sendEvent('closed');
		_listeners = null;
	}
	
	/** 将监听器压入,待发生事件时触发,监听器原型:
	 *  function listen(eventStr) */
	function _listener(_l) {
		if (typeof _l =='function') {
			_listeners.push(_l);
		}
	}
	
	/**
	 * 事件类型:
	 * close	- 关闭并释放内存
	 * show		- 组件被显示
	 * ok		- 确定按钮按下
	 * cancel	- 取消按钮按下
	 * closed	- 当组件被完全关闭后发出
	 * @param {} eventStr
	 */
	function _sendEvent(eventStr) {
		if (!_listeners) return;
		for (var i=_listeners.length-1; i>=0; i--) {
			_listeners[i](eventStr);
		}
	}
	
	return {
		 'show'		: _show
		,'close'	: _close
		,'objArrays': _objArrs
		,'listener'	: _listener
		,'addTag'	: _addTag
	};
}

/**
 * 修改_tagArr中所有的tag对象的样式为_styleArr中的值
 */
function changeStylesWithArr(_tagArr, _styleArr) {
	for (var tag in _tagArr) {
		if (tag != 'length' && _tagArr[tag] && _tagArr[tag].style) {
			for (var style in _styleArr) {
				if (style != 'length' && _styleArr[style]) {
					try {
						_changeStyle(_tagArr[tag], style, _styleArr[style]);
					} catch (e) {
					}
				}
			}
		}
	}
}

function _change_IE_Filter(_tag, _key, _value) {
	var FIELD_NAME = '_ie_filter_cache';

	if (!_tag[FIELD_NAME]) {
		_tag[FIELD_NAME] = {};
	}
	
	var _cache = _tag[FIELD_NAME];
	_cache[_key] = _value;
	
	var filters = [];
	for (var k in _cache) {
		filters.push(_cache[k]);
	}

	_tag.style.filter = filters.join(' ');
}

/** 通常是: [_tag].style.[_sname] = _svalue, 
 *  如果有特殊的属性, 需要单独设计算法
 *  失败会抛出异常 */
function _changeStyle(_tag, _sname, _svalue) {
	if (_sname=='boxShadow') {
		if (_ie) {
			_change_IE_Filter(_tag, _sname, "progid:DXImageTransform.Microsoft.Shadow" +
											"(" + _svalue + ")");
		} else {
			_tag.style.boxShadow = _svalue;
		}
		
	} else if (_sname=='opacity') {
		if (_ie) {
			_change_IE_Filter(_tag, _sname, 'alpha(opacity='+_svalue+')');
		} else {
			_tag.style.opacity = _svalue/100;
		}
		
	} else {
		_tag.style[_sname] = _svalue;
	}
}

/** 导出函数 */
window.createCssDialog		= createCssDialog;
window.changeStylesWithArr	= changeStylesWithArr;
})();