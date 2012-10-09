select * from 

#if ((id+1) == 2 )
	--如果id设置为1 则会显示这里
	table1 ${id}
#end

#if ((id+1) != 2)
	--如果id设置了别的值: ${id }
	table2
#end

#for(sub_list : list1)
	#for(item : sub_list)
		list1::${item }
	#end
#end

#for(item : list2)
	list2::${item }
#end

#for(item : list3)
	list3::${item }
#end