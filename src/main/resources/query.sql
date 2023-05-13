select *
from profile;
select *
from category;
select *
from region;
select *
from article;
select *
from attach;

update article
set status = 'PUBLISHED';


-- get article by id
select a.id                                                                              as articleId,
       a.attach_id                                                                       as attachId,
       a.category_id                                                                     as categoryId,
       a.content                                                                         as content,
       a.title                                                                           as title,
       a.description                                                                     as description,
       a.created_date                                                                    as createdDate,
       a.moderator_id                                                                    as moderatorId,
       a.region_id                                                                       as regionId,
       (case 'en' when 'uz' then r.name_uz when 'en' then r.name_eng else r.name_ru end) as regionName,
       a.category_id                                                                     as categoryId,
       (case 'en' when 'uz' then c.name_uz when 'en' then c.name_eng else c.name_ru end) as categoryName
from article as a
         inner join region as r on r.id = a.region_id
         inner join category as c on c.id = a.category_id