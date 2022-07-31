--1
select --a.mi_code
decode(fund_type,'3','职工','91','居民','92','居民','93','居民','31','离休','32','医照','33','超转') 险种
,a.id 交易流水号
,id_card 身份证号
,pname 姓名
--,payment_date 支付时间
,fee_date 费用发生时间
,subject_name 项目名称
,subject_code 项目代码
,fee_amnt 项目金额
,a.m_in 交易医保内
,b_m_in 项目医保内
,unite_in 交易统筹
,a.large_in 交易大额
,a.supply_in 交易补充
,a.deformity_in 交易残疾
,a.offi_in 交易单位补充
,price 单价
,amount 数量
,section_name 科室
,doctor_name 医师
,diagnosis_name 诊断
,decode(fund_type,'3',decode(is_quitwork,'1','在职','2','退休')) 在职退休
from (select/*+driving_site(a) parallel(a 16)*/count(distinct subject_code) over(partition by id) cnt, a.* from tmp20220722_rtd_de a where subject_code in('ECABA001','ECABC002')) a
where cnt>1
order by fee_date
;

--2
select --a.mi_code
decode(fund_type,'3','职工','90','居民','31','离休','32','医照','33','超转') 险种
,a.id 交易流水号
,id_card 身份证号
,pname 姓名
--,payment_date 支付时间
,fee_date 费用发生时间
,subject_name 项目名称
,subject_code 项目代码
,fee_amnt 项目金额
,a.m_in 交易医保内
,b_m_in 项目医保内
,unite_in 交易统筹
,a.large_in 交易大额
,a.supply_in 交易补充
,a.deformity_in 交易残疾
,a.offi_in 交易单位补充
,price 单价
,amount 数量
,section_name 科室
,doctor_name 医师
,diagnosis_name 诊断
,decode(fund_type,'3',decode(is_quitwork,'1','在职','2','退休')) 在职退休
from (select/*+driving_site(a) parallel(a 16)*/count(distinct subject_code) over(partition by id) cnt, a.* from tmp20220722_rtd_de a where subject_code in('HXP73303','HXP73304')) a
--where cnt>1
order by fee_date
;

--4.
select --a.mi_code
decode(fund_type,'3','职工','91','居民','92','居民','93','居民','31','离休','32','医照','33','超转') 险种
,a.id 交易流水号
,id_card 身份证号
,pname 姓名
--,payment_date 支付时间
,fee_date 费用发生时间
,subject_name 项目名称
,subject_code 项目代码
,fee_amnt 项目金额
,a.m_in 交易医保内
,b_m_in 项目医保内
,unite_in 交易统筹
,a.large_in 交易大额
,a.supply_in 交易补充
,a.deformity_in 交易残疾
,a.offi_in 交易单位补充
,price 单价
,amount 数量
,section_name 科室
,doctor_name 医师
,diagnosis_name 诊断
,decode(fund_type,'3',decode(is_quitwork,'1','在职','2','退休')) 在职退休
from (select/*+driving_site(a) parallel(a 16)*/a.* from tmp20220722_rtd_de a where subject_code not in('ECABA001','ECABC002','HXP73303','HXP73304')) a
order by fee_date
;

