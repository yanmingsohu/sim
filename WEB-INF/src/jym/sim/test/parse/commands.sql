select * from table

#if (id+1 == 2)
	如果id设置为1 则会显示这里
#end

#if (id+1 != 2)
	如果id设置了别的值: ${id }
#end

#for(a : b)
${a }
#end