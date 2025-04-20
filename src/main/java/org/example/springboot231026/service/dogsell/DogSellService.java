package org.example.springboot231026.service.dogsell;


import org.example.springboot231026.domain.dog.DogSell;
import org.example.springboot231026.domain.dog.DogSellImage;
import org.example.springboot231026.domain.dog.DogSellImageRepository;
import org.example.springboot231026.domain.dog.DogSellRepository;
import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.posts.Posts;
import org.example.springboot231026.dto.dogsell.*;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.page.PageRequestDTO;
import org.example.springboot231026.dto.page.PageResponseDTO;
import org.example.springboot231026.dto.post.PostsListResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DogSellService {

    @Autowired
    private DogSellRepository dsr;

    @Autowired
    private DogSellImageRepository dsir;

    @Autowired
    private WishNumService wns;

    
    //등록
    @Transactional
    public Long register(DogSellDTO dto){
        //System.out.println("service-dogsell클래스 DogSellService register() 진입");

        Map<String, Object> entitymap = dtoToEntity(dto);
        DogSell dsentity = (DogSell) entitymap.get("dogsell");
        //System.out.println("service-dogsell클래스 DogSellService register() 진입 DogSell 엔티티 Map에서 꺼내오기 -> "+ dsentity.toString());

        List<DogSellImage> dsientity = (List<DogSellImage>) entitymap.get("dsImageList");
        //System.out.println("service-dogsell클래스 DogSellService register() 진입 DogSellImage 엔티티 Map에서 꺼내오기 -> "+ dsientity.toString());

        dsr.save(dsentity);

        dsientity.forEach(di ->{dsir.save(di);});
        return dsentity.getDno();
    }


    @Transactional
    //목록
    public List<DogSellListDTO> list(){
        //System.out.println("service-dogsell클래스 DogSellService list() 진입");
        List<Object []> objectArray = dsr.getDogSellAllImage();

        List<DogSellListDTO> listDto = new ArrayList<>();

        for(Object [] array : objectArray){
            DogSellListDTO result = entityToDtoDogListSell((DogSell)array[0], (DogSellImage)array[1]);

            listDto.add(result);
        }
        return listDto;
    }


    @Transactional
    //1개조회
    public DogSellReadDTO read(Long dno){
        //System.out.println("service-dogsell클래스 DogSellService read() 진입 - 파라미터 등록번호 -> "+ dno);

        //따로따로 불러온다.
        Optional<DogSell> entity = dsr.findById(dno);

        DogSellReadDTO result = null;
        
        //찜강아지 존재할경우
        if(entity.isPresent()) {
            //System.out.println("service-dogsell클래스 DogSellService read() 진입 - 분양강아지 정보 -> " + entity.get().toString());

            List<DogSellImage> entityI = dsir.findByDno(dno);
            //System.out.println("service-dogsell클래스 DogSellService read() 진입 - 분양강아지 이미지정보 -> " + entityI.toString());
            result = entityToDtoDogReadSell((DogSell) entity.get(), entityI);

        }
        return result;
    }


    //수정
    @Transactional
    //1개조회
    public void modify(DogSellModifyDTO dsModifyDto){
        //System.out.println("service-dogsell클래스 DogSellService modify() 진입");

        DogSellReadDTO before =read(dsModifyDto.getDno());

        //System.out.println("service-dogsell클래스 DogSellService modify() 진입 수정전 DTO -> "+ before.toString());

        Map<String, Object> map = dtoToEntityModifyDto(dsModifyDto);
        //map.put("dsImageList", ientityList);
        //map.put("dogsell", entity);//변경된 entity 지정

        DogSell dsEntity = (DogSell)map.get("dogsell");

        //System.out.println("service-dogsell클래스 DogSellService modify() 진입 수정후 DogSell -> "+ dsEntity);

        if(map.get("dsImageList") !=null){
            //System.out.println("service-dogsell클래스 DogSellService modify() 진입 Map<> DogSellImage 들어있을때 진입 ");

            List<DogSellImage> dsiEntity = (List<DogSellImage>)map.get("dsImageList");

            //System.out.println("service-dogsell클래스 DogSellService modify() 진입 수정후 DogSellImage -> "+ dsiEntity);

        }
    }


    //삭제
    @Transactional
    public void remove(Long dno, String name) {
    //public void remove(Long dno, String email) {

        //System.out.println("service-dogsell클래스 DogSellService remove() 진입 강아지분양번호 -> "+dno+", 회원 name -> "+ name);
        //System.out.println("service-dogsell클래스 DogSellService remove() 진입 강아지분양번호 -> "+dno+", 회원 email -> "+ email);

        Optional<DogSell> o=dsr.findById(dno);
        List<DogSellImage> oi = dsir.findByDno(dno);

        //위시번호에서 삭제
        wns.wishlistremoveAll(dno);

        if(oi.size() == 0){
            //System.out.println("service-dogsell클래스 DogSellService remove() 진입 이미지는 존재하지 않을경우 진입");
            dsr.deleteById(dno);

        }
        if(oi.size() != 0){
            //System.out.println("service-dogsell클래스 DogSellService remove() 진입 이미지는 존재할경우 진입 2개 테이블 모두 삭제처리");
            for(DogSellImage dsi : oi) {
                dsir.deleteById(dsi.getInum()); //먼저 이미지부터 삭제한다.
            }
            dsr.deleteById(dno);

        }
    }

    //회원탈퇴시 분양글과 이미지삭제
    @Transactional
    public boolean removeDeleteMember(String name) {
        //System.out.println("service-dogsell클래스 DogSellService removeDeleteMember() 진입 회원 name -> "+ name);
        List<Long> dnoList = dsr.getDogSellDnoByName(name);

        boolean result = true;

        //작성한 분양글이 있다면
        if(dnoList !=null && dnoList.size() != 0) {
            for (Long dno : dnoList) {
                //System.out.println("service-dogsell클래스 DogSellService removeDeleteMember() 진입 "+
                       // "작성한 분양글이 있는 회원의 경우 분양댓글번호 List로 가져오기 -> " + dno);

                Optional<DogSell> o = dsr.findById(dno);

                List<DogSellImage> oi = dsir.findByDno(dno);

                //위시번호에서 삭제
                wns.wishlistremoveAll(dno);

                //분양글 이미지 없을때
                if (oi.size() == 0) {
                    //System.out.println("service-dogsell클래스 DogSellService removeDeleteMember() 진입 이미지는 존재하지 않을경우 진입");
                    dsr.deleteById(dno);

                }

                //분양글이미지 있을때
                if (oi.size() != 0) {
                    //System.out.println("service-dogsell클래스 DogSellService removeDeleteMember() 진입 이미지는 존재할경우 진입 2개 테이블 모두 삭제처리");
                    for (DogSellImage dsi : oi) {
                        dsir.deleteById(dsi.getInum()); //먼저 이미지부터 삭제한다.
                    }
                    dsr.deleteById(dno);
                }
            }
            result = true;

        }else{
            //System.out.println("service-dogsell클래스 DogSellService removeDeleteMember() 진입 "+
                   // "작성한 분양글이 없는 회원일경우");
            result = true;
        }
        return result;
    }


    //분양완료글 진입
    @Transactional
    public boolean complete(Long dno) {
        //System.out.println("service-dogsell클래스 DogSellService complete() 진입");
        Optional<DogSell> entity = dsr.findById(dno);

        boolean result =false;
        if(entity.isPresent()){
            entity.get().changeComplete("true");
            result = true;
        }
        return result;
    }


    //분양품종검색
    public List<DogSellListDTO> searchBreed(String searchBreed) {
        //System.out.println("service-dogsell클래스 DogSellService searchBreed() 진입");

        List<Object []> objectArray = dsr.getDogSellByType(searchBreed);

        List<DogSellListDTO> listDto = new ArrayList<>();

        if(objectArray.size() <=0 || objectArray ==null){

            //System.out.println("service-dogsell클래스 DogSellService searchBreed() 진입 존재하는 품종이 없을경우 진입");
            return  listDto;

        }else {

            for (Object[] array : objectArray) {
                DogSellListDTO result = entityToDtoDogListSell((DogSell) array[0], (DogSellImage) array[1]);

                ////System.out.println("service-dogsell클래스 DogSellService searchBreed() 진입 존재하는 품종이 " +
                               // " 있을경우 진입 DogSellListDTO-> "+result.toString());
                listDto.add(result);
            }
            return listDto;
        }
    }

    public Map<String, Object> dtoToEntity(DogSellDTO dsDto){
        //System.out.println("service-dogsell클래스 DogSellService dtoToEntity() 진입");

        Map<String, Object> map = new HashMap<>();

        DogSell entity = DogSell.builder()//DB넣기 전이기에 3개는 없다.
                .writer(dsDto.getWriter())
                .name(dsDto.getName())
                .title(dsDto.getTitle())
                .price((int)dsDto.getPrice())
                .age(dsDto.getAge())
                .content(dsDto.getContent())
                .weight(dsDto.getWeight())
                .type(dsDto.getType())
                .health(dsDto.getHealth())
                .gender(dsDto.getGender())
                .build();

        map.put("dogsell", entity);

        List<DogSellImageDTO> iList = dsDto.getDsiDtoList();

        //DogSellImageDTO
        if(iList !=null && iList.size()>0){
            //System.out.println("service-dogsell클래스 DogSellService dtoToEntity() 진입 - DogSellDTO의 멤버필드 List<DogSellImageDTO> 존재할경우 진입");

            List<DogSellImage> ientityList= iList.stream()
                    .map(iDto ->{

                        DogSellImage ientity = DogSellImage.builder()
                                .uuid(iDto.getUuid())
                                .path(iDto.getPath())
                                .inum(iDto.getInum())
                                .imgName(iDto.getImgName())
                                .dogsell(entity)
                                .build();
                        return ientity;
                    }).collect(Collectors.toList()); //엔티티 List<DogSellImage> 만든다.

            map.put("dsImageList", ientityList);
        }
        return map; //DogSell 은 dogsell로 담고 List<DogSellImage>는 dsImageList 이름으로 데이터를 담는다.
    }

    //수정 DTO -> ENTITY
    public Map<String, Object> dtoToEntityModifyDto(DogSellModifyDTO dsModifyDto){
        //System.out.println("service-dogsell클래스 DogSellService dtoToEntityModifyDto() 진입 -파라미터 DogSellModifyDTO");

        Map<String, Object> map = new HashMap<>();
        Optional<DogSell> o = dsr.findById(dsModifyDto.getDno());

        DogSell entity = o.get().change(
                dsModifyDto.getTitle(), dsModifyDto.getGender(),dsModifyDto.getType(),
                dsModifyDto.getName(), dsModifyDto.getContent(), dsModifyDto.getHealth(),
                (Double)dsModifyDto.getAge(), (Double)dsModifyDto.getWeight(), (int)dsModifyDto.getPrice()
               );

        map.put("dogsell", entity);//변경된 entity 지정

        List<DogSellImageDTO> iList = dsModifyDto.getDsiDtoList();


        List<DogSellImage> diList = dsir.findByDno(dsModifyDto.getDno());
        //System.out.println("service-dogsell클래스 DogSellService dtoToEntityModifyDto() 진입 - List<DogSellImage> 출력 -> "+diList.toString());


        //DogSellImage엔티티로 변환
        if(iList !=null && iList.size()>0){
            //System.out.println("service-dogsell클래스 DogSellService dtoToEntityModifyDto() 진입 - List<DogSellImageDTO> 존재할경우 진입");

            //기존 이미지들은 삭제처리
           /* for(DogSellImageDTO dsi: iList) {
                dsir.deleteById(dsi.getInum());//@Id 는 Dno 가 아니라 inum 지정되어있다.
            }*/
            for(DogSellImage dsi: diList) {
                //System.out.println("service-dogsell클래스 DogSellService dtoToEntityModifyDto() 진입 - List<DogSellImageDTO> dsi.getInum()->  "+dsi.getInum());

                dsir.deleteById(dsi.getInum());//@Id 는 Dno 가 아니라 inum 지정되어있다.
            }



            List<DogSellImage> ientityList= iList.stream()
                    .map(iDto ->{

                        DogSellImage ientity = DogSellImage.builder()
                                .uuid(iDto.getUuid())
                                .path(iDto.getPath())
                                //.inum(iDto.getInum())
                                .imgName(iDto.getImgName())
                                .dogsell(entity)
                                .build();

                        dsir.save(ientity);//기존삭제했기에 db추가해야한다.
                        
                        return ientity;
                    }).collect(Collectors.toList()); //엔티티 List<DogSellImage> 만든다.

            map.put("dsImageList", ientityList);
        }

        //DogSellImage엔티티로 변환
        if(iList ==null && iList.size()==0) {
            //System.out.println("service-dogsell클래스 DogSellService dtoToEntityModifyDto() 진입 - List<DogSellImageDTO> 존재하지 않을경우 진입");

            iList=null;
        }

            return map; //DogSell 은 dogsell로 담고 List<DogSellImage>는 dsImageList 이름으로 데이터를 담는다.
    }


    public DogSellListDTO entityToDtoDogSell(DogSell dsEntity) {
        //System.out.println("service-dogsell클래스 DogSellService entityToDtoDogSell() 진입");

        DogSellListDTO dsDto = DogSellListDTO.builder()
                .age(dsEntity.getAge())
                .type(dsEntity.getType())
                .health(dsEntity.getHealth())
                .gender(dsEntity.getGender())
                .createdDate(dsEntity.getCreatedDate())
                .modifiedDate(dsEntity.getModifiedDate())
                .price(dsEntity.getPrice())
                .content(dsEntity.getContent())
                .name(dsEntity.getName())
                .dsiDtoList(null)
                .build();
        //System.out.println("service-dogsell클래스 DogSellService entityToDtoDogSell() 진입 DogSellListDTO-> " + dsDto.toString());


        return dsDto;
    }

    public DogSellListDTO entityToDtoDogListSell(DogSell dsEntity, DogSellImage dsiEntity) {
        //System.out.println("service-dogsell클래스 DogSellService entityToDtoDogSell() 진입");

            if(dsiEntity ==null) {
                //System.out.println("service-dogsell클래스 DogSellService entityToDtoDogSell() 진입 DogSellImage 존재하지 않을떄");
                DogSellListDTO dsDto = DogSellListDTO.builder()
                        .age(dsEntity.getAge())
                        .weight(dsEntity.getWeight())
                        .type(dsEntity.getType())
                        .health(dsEntity.getHealth())
                        .gender(dsEntity.getGender())
                        .dno(dsEntity.getDno()).membername(dsEntity.getWriter())
                        .createdDate(dsEntity.getCreatedDate())
                        .modifiedDate(dsEntity.getModifiedDate())
                        .dsiDtoList(null)
                        .complete(dsEntity.getComplete())
                        .name(dsEntity.getName())
                        .content(dsEntity.getContent())
                        .price(dsEntity.getPrice())
                        .build();
                //System.out.println("service-dogsell클래스 DogSellService entityToDtoDogSell() 진입 DogSellListDTO-> " + dsDto.toString());
                return dsDto;
            }

            if(dsiEntity !=null) {

                //System.out.println("service-dogsell클래스 DogSellService entityToDtoDogSell() 진입 DogSellImage가 존재할때");
                DogSellImageDTO dsiDto = DogSellImageDTO.builder()
                        .uuid(dsiEntity.getUuid())
                        .imgName(dsiEntity.getImgName())
                        .path(dsiEntity.getPath())
                        .inum(dsiEntity.getInum())
                        .build();
                //System.out.println("service-dogsell클래스 DogSellService entityToDtoDogSell() 진입 DogSellImageDTO-> " + dsiDto.toString());


            List<DogSellImageDTO> imageDtoList =new ArrayList<>();

            imageDtoList.add(dsiDto);

            DogSellListDTO dsDto = DogSellListDTO.builder()
                .age(dsEntity.getAge())
                .weight(dsEntity.getWeight())
                .type(dsEntity.getType())
                .health(dsEntity.getHealth())
                .gender(dsEntity.getGender())
                .dno(dsEntity.getDno())
                .membername(dsEntity.getWriter())
                .modifiedDate(dsEntity.getModifiedDate())
                .createdDate(dsEntity.getCreatedDate())
                .dsiDtoList(imageDtoList)
                .complete(dsEntity.getComplete())
                .price(dsEntity.getPrice())
                .content(dsEntity.getContent())
                .name(dsEntity.getName())
                .build();
            //System.out.println("service-dogsell클래스 DogSellService entityToDtoDogSell() 진입 DogSellListDTO-> "+dsDto.toString());
            return dsDto;

            }
            return null;
    }

    //public DogSellReadDTO entityToDtoDogReadSell(DogSell dsEntity, DogSellImage dsiEntity) {
    public DogSellReadDTO entityToDtoDogReadSell(DogSell dsEntity, List<DogSellImage> dsiEntity) {
        //System.out.println("service-dogsell클래스 DogSellService DogSellReadDTO() 진입");

        //System.out.println("service-dogsell클래스 DogSellService DogSellReadDTO() 진입 DogSell -> "+dsEntity.toString());

        //System.out.println("service-dogsell클래스 DogSellService DogSellReadDTO() 진입 List<DogSellImage> -> "+ dsiEntity.toString());

        //if(dsiEntity ==null) {
        if(dsiEntity ==null && dsiEntity.size() == 0) {
            //System.out.println("service-dogsell클래스 DogSellService entityToDtoDogSell() 진입 List<DogSellImage> 존재하지 않을떄");

//Long dno; double weight, age, price;  String writer, content,title, gender, type, name, health; //11개
//LocalDateTime createdDate, modifiedDate; //2개
// List<DogSellImageDTO> dsiDtoList;//1개
            DogSellReadDTO dsreadDto = DogSellReadDTO.builder() //14개필드
                    .age(dsEntity.getAge())
                    .weight(dsEntity.getWeight())
                    .type(dsEntity.getType())
                    .health(dsEntity.getHealth())
                    .gender(dsEntity.getGender())
                    .name(dsEntity.getName())
                    .price(dsEntity.getPrice())
                    .content(dsEntity.getContent())
                    .writer(dsEntity.getWriter())
                    .dno(dsEntity.getDno())
                    .complete(dsEntity.getComplete())
                    .title(dsEntity.getTitle())
                    .createdDate(dsEntity.getCreatedDate())
                    .modifiedDate(dsEntity.getModifiedDate())
                    .dsiDtoList(null)
                    .build();
            //System.out.println("service-dogsell클래스 DogSellService entityToDtoDogSell() 진입 DogSellReadDTO-> " + dsreadDto.toString());
            return dsreadDto;
        }

        //if(dsiEntity !=null) {
        if(dsiEntity !=null&& dsiEntity.size() > 0) {

            //System.out.println("service-dogsell클래스 DogSellService entityToDtoDogSell() 진입 DogSellImage가 존재할때");
            List<DogSellImageDTO> dsiDtoList = new ArrayList<>();

            //Long inum; String uuid,imgName,path; getImageURL() getThumbnailURL()
            for(DogSellImage dsiDto : dsiEntity){
                DogSellImageDTO dto = DogSellImageDTO.builder()
                        .uuid(dsiDto.getUuid())
                        .imgName(dsiDto.getImgName())
                        .path(dsiDto.getPath())
                        .inum(dsiDto.getInum()) //등록된 데이터이므로 inum존재한다.
                        .build();
                //System.out.println("service-dogsell클래스 DogSellService entityToDtoDogSell() 진입 DogSellImage가 존재할때 DogSellImageDTO -> "+dto.toString());

                dsiDtoList.add(dto);
            }

            DogSellReadDTO dsreadDto = DogSellReadDTO.builder()
                    .age(dsEntity.getAge())
                    .weight(dsEntity.getWeight())
                    .type(dsEntity.getType())
                    .health(dsEntity.getHealth())
                    .gender(dsEntity.getGender())
                    .name(dsEntity.getName())
                    .price(dsEntity.getPrice())
                    .content(dsEntity.getContent())
                    .writer(dsEntity.getWriter())
                    .dno(dsEntity.getDno())
                    .complete(dsEntity.getComplete())
                    .title(dsEntity.getTitle())
                    .createdDate(dsEntity.getCreatedDate())
                    .modifiedDate(dsEntity.getModifiedDate())
                    .dsiDtoList(dsiDtoList)
                    .build();
            //System.out.println("service-dogsell클래스 DogSellService entityToDtoDogSell() 진입 DogSellReadDTO-> "+dsreadDto.toString());
            return dsreadDto;

        }
        return null;
    }

    //관리자
    public List<DogSellListDTO> adminDogSellList(){
        ////System.out.println("service-dogsell클래스 DogSellService adminDogSellList() 진입 " +
               // " List<DogSellListDTO> 반환 관리자페이지위한 ");

        List<Object [] > objectArray =dsr.getDogSellByModifiedDate();
        List<DogSellListDTO> listDto = new ArrayList<>();

        for(Object [] array : objectArray){
            DogSellListDTO result = entityToDtoDogListSell((DogSell)array[0], (DogSellImage)array[1]);

            listDto.add(result);
        }
        return listDto;
    }


    //관리자페이지에서 특정날짜게시글수
    public Long getCountLocalDate(LocalDate localDate){
        //System.out.println("service-dogsell클래스 DogSellService getCountLocalDate() 진입 시작시간 -> "+localDate.atTime(LocalTime.MIN)+"종료시간 -> "+localDate.atTime(LocalTime.MAX));

        Long count = dsr.getCountLocalDate(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX));
        //System.out.println("service-dogsell클래스 DogSellService getCountLocalDate() 진입 localDate 분양글수 -> "+ count);
        return count;
    }
    
    //관리자페이지
    @Transactional
    public PageResponseDTO<DogSellListDTO> getListAdminCreatedDate(PageRequestDTO requestDTO, LocalDate localDate) {
        //System.out.println("service-dogsell클래스 DogSellService getListAdminCreatedDate() 진입 ");

        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();

        //Pageable p = requestDTO.getPageable("createdDate");

        Pageable p = PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());

        //날짜까지 추가
        Page<DogSell> page = dsr.searchDogSellAllCreatedDate(types, keyword, p,localDate);

        List<Object []> objectArray =dsr.getDogSellByModifiedDate();
        List<DogSellListDTO> listDto = new ArrayList<>();

        //DogSellListDTO  entityToDtoDogListSell

        listDto = page.getContent().stream()
                .map(entity -> DogSellListDTO.builder()
                        .createdDate(entity.getCreatedDate())
                        .modifiedDate(entity.getModifiedDate())
                        .dno(entity.getDno())
                        .price(entity.getPrice())
                        .age(entity.getAge())
                        .gender(entity.getGender())
                        .name(entity.getName())
                        .health(entity.getHealth())
                        .content(entity.getContent())
                        .type(entity.getType())
                        .weight(entity.getWeight())
                        .complete(entity.getComplete())
                        .membername(entity.getWriter())
                        .dsiDtoList(entityToDto(dsir.findByDno(entity.getDno()))).build())
                .collect(Collectors.toList());

        return PageResponseDTO.<DogSellListDTO>withAll()
                .requestDTO(requestDTO)
                .dtoList(listDto)
                .total((int)page.getTotalElements())
                .build();

    }

    public List<DogSellImageDTO> entityToDto(List<DogSellImage> entityList){
        //System.out.println("service-dogsell클래스 DogSellService entityToDto() 진입");
        List<DogSellImageDTO> dtoList =new ArrayList<>();

        for(DogSellImage entity : entityList) {
            DogSellImageDTO dto = new DogSellImageDTO();

            dto.setUuid(entity.getUuid());
            dto.setPath(entity.getPath());
            dto.setImgName(entity.getImgName());
            dto.setInum(entity.getInum());
            dtoList.add(dto);
        }
        return dtoList;
    }

    //분양글목록
    @Transactional
    public PageResponseDTO<DogSellListDTO> list(PageRequestDTO requestDTO){
        //System.out.println("service-dogsell클래스 DogSellService list() 진입 파라미터 PageRequestDTO -> "+ requestDTO.toString());

        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();
        Pageable p = requestDTO.getPageable("dno");

        Page<DogSell> page = dsr.searchDogSellAll(types, keyword, p);

        //System.out.println("service-dogsell클래스 DogSellService list() 진입 파라미터 DogSell엔티티 -> "+ page.getContent());

        List<DogSellListDTO> listDto = new ArrayList<>();

        listDto = page.getContent().stream()
                .map(entity -> DogSellListDTO.builder()
                        .createdDate(entity.getCreatedDate())
                        .modifiedDate(entity.getModifiedDate())
                        .dno(entity.getDno())
                        .price(entity.getPrice())
                        .age(entity.getAge())
                        .gender(entity.getGender())
                        .name(entity.getName())
                        .health(entity.getHealth())
                        .content(entity.getContent())
                        .type(entity.getType())
                        .weight(entity.getWeight())
                        .complete(entity.getComplete())
                        .membername(entity.getWriter())
                        .dsiDtoList(entityToDto(dsir.findByDno(entity.getDno()))).build())
                .collect(Collectors.toList());

        return PageResponseDTO.<DogSellListDTO>withAll()
                .requestDTO(requestDTO)
                .dtoList(listDto)
                .total((int)page.getTotalElements())
                .build();
    }


//System.out.println("service-dogsell클래스 DogSellService register() 진입");
}
