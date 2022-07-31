select ','||column_name from all_tab_columns where table_name = upper('tmp20210924_inh') order by column_id
;
desc a_rtd_detail
drop table tmp20210924_rtd;
create table tmp20210924_rtd as
select '&1' dd
,fund_type
,upper(subject_code) subject_code
,regexp_replace(subject_name,'\s|,',' ') subject_name
,sum(amount) amount
,sum(fee_amnt) fee_amnt
,sum(b.m_in) m_in
,sum(b.m_out) m_out
,b.price
,f_level
,sp_scale
from a_rtd_charge a,a_rtd_detail b,m_cure_catalog_hos c
where a.id=b.charge_id
and upper(subject_code)=upper(c.code(+))
and a.fund_type in('3','91','92','93')
and divide_type not in('8','9')
and a.state='1'
and a.m_class in('11','19')
and a.deal_divide_flag <> '2'
and regexp_like(b.fee_type,'((0770)|(089[012])|(094[012])|(9903))')
AND A.in_formal_DATE >= to_date('&1','yyyymmddhh24mi') and a.in_formal_DATE < to_date('&1','yyyymmddhh24mi') + &2/24/60
and 1<>1
group by fund_type,upper(subject_code) ,subject_name,b.price,f_level,sp_scale
;
drop table tmp20210924_inh;
create table tmp20210924_inh as select * from tmp20210924_rtd where rownum<1;
;
select * from tmp20210924_rtd where rownum<10;
select /*+parallel(a 16)*/substr(dd,1,4) ���,decode(fund_type,'3','ְ��','����') ����,subject_code ��Ŀ����,subject_name ��Ŀ����,sum(amount) ����,sum(fee_amnt) ���,sum(m_in) ҽ����,sum(m_out) ҽ����
,price ����,f_level �շ���Ŀ�ȼ�,sp_scale �Ը�����
from tmp20210924_rtd a
group by decode(fund_type,'3','ְ��','����'),fund_type,subject_code ,subject_name,price,f_level,sp_scale
,substr(dd,1,4)
;

select /*+parallel(a 16)*/substr(dd,1,4) ���,fund_type ����,subject_code ��Ŀ����,subject_name ��Ŀ����,sum(amount) ����,sum(fee_amnt) ���,sum(m_in) ҽ����,sum(m_out) ҽ����
,price ����,f_level �շ���Ŀ�ȼ�,sp_scale �Ը�����
from tmp20210924_inh a
group by fund_type,fund_type,subject_code ,subject_name,price,f_level,sp_scale
,substr(dd,1,4)
;

---------------------------------------------
select /*+parallel(a 16)*/substr(dd,1,4) ���,decode(fund_type,'3','ְ��','����') ����,subject_code ��Ŀ����,subject_name ��Ŀ����,sum(amount) ����,sum(fee_amnt) ���,sum(m_in) ҽ����,sum(m_out) ҽ����
,price ����
from tmp20210924_rtd a
where substr(dd,1,4)='&1'
group by decode(fund_type,'3','ְ��','����'),fund_type,subject_code ,subject_name,price,f_level,sp_scale
,substr(dd,1,4)
;

select /*+parallel(a 16)*/substr(dd,1,4) ���,fund_type ����,subject_code ��Ŀ����,subject_name ��Ŀ����,sum(amount) ����,sum(fee_amnt) ���,sum(m_in) ҽ����,sum(m_out) ҽ����
,price ����
from tmp20210924_inh a
where substr(dd,1,4)='&1'
group by fund_type,fund_type,subject_code ,subject_name,price,f_level,sp_scale
,substr(dd,1,4)
;

