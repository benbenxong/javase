--1
select --a.mi_code
decode(fund_type,'3','ְ��','91','����','92','����','93','����','31','����','32','ҽ��','33','��ת') ����
,a.id ������ˮ��
,id_card ���֤��
,pname ����
--,payment_date ֧��ʱ��
,fee_date ���÷���ʱ��
,subject_name ��Ŀ����
,subject_code ��Ŀ����
,fee_amnt ��Ŀ���
,a.m_in ����ҽ����
,b_m_in ��Ŀҽ����
,unite_in ����ͳ��
,a.large_in ���״��
,a.supply_in ���ײ���
,a.deformity_in ���ײм�
,a.offi_in ���׵�λ����
,price ����
,amount ����
,section_name ����
,doctor_name ҽʦ
,diagnosis_name ���
,decode(fund_type,'3',decode(is_quitwork,'1','��ְ','2','����')) ��ְ����
from (select/*+driving_site(a) parallel(a 16)*/count(distinct subject_code) over(partition by id) cnt, a.* from tmp20220722_rtd_de a where subject_code in('ECABA001','ECABC002')) a
where cnt>1
order by fee_date
;

--2
select --a.mi_code
decode(fund_type,'3','ְ��','90','����','31','����','32','ҽ��','33','��ת') ����
,a.id ������ˮ��
,id_card ���֤��
,pname ����
--,payment_date ֧��ʱ��
,fee_date ���÷���ʱ��
,subject_name ��Ŀ����
,subject_code ��Ŀ����
,fee_amnt ��Ŀ���
,a.m_in ����ҽ����
,b_m_in ��Ŀҽ����
,unite_in ����ͳ��
,a.large_in ���״��
,a.supply_in ���ײ���
,a.deformity_in ���ײм�
,a.offi_in ���׵�λ����
,price ����
,amount ����
,section_name ����
,doctor_name ҽʦ
,diagnosis_name ���
,decode(fund_type,'3',decode(is_quitwork,'1','��ְ','2','����')) ��ְ����
from (select/*+driving_site(a) parallel(a 16)*/count(distinct subject_code) over(partition by id) cnt, a.* from tmp20220722_rtd_de a where subject_code in('HXP73303','HXP73304')) a
--where cnt>1
order by fee_date
;

--4.
select --a.mi_code
decode(fund_type,'3','ְ��','91','����','92','����','93','����','31','����','32','ҽ��','33','��ת') ����
,a.id ������ˮ��
,id_card ���֤��
,pname ����
--,payment_date ֧��ʱ��
,fee_date ���÷���ʱ��
,subject_name ��Ŀ����
,subject_code ��Ŀ����
,fee_amnt ��Ŀ���
,a.m_in ����ҽ����
,b_m_in ��Ŀҽ����
,unite_in ����ͳ��
,a.large_in ���״��
,a.supply_in ���ײ���
,a.deformity_in ���ײм�
,a.offi_in ���׵�λ����
,price ����
,amount ����
,section_name ����
,doctor_name ҽʦ
,diagnosis_name ���
,decode(fund_type,'3',decode(is_quitwork,'1','��ְ','2','����')) ��ְ����
from (select/*+driving_site(a) parallel(a 16)*/a.* from tmp20220722_rtd_de a where subject_code not in('ECABA001','ECABC002','HXP73303','HXP73304')) a
order by fee_date
;

