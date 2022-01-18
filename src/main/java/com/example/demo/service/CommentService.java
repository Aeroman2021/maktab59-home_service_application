package com.example.demo.service;

import com.example.demo.dto.Comment.InputCommentDto;
import com.example.demo.dto.Comment.OutputCommentDto;
import com.example.demo.model.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.core.AbstractCRUD;
import com.example.demo.service.core.EntityConvertor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class CommentService extends AbstractCRUD<Comment,Integer> implements
        EntityConvertor<InputCommentDto,Comment, OutputCommentDto> {

    private final CommentRepository commentRepository;
    private final OrderService orderService;
    @PostConstruct
    public void init(){
        setJpaRepository(commentRepository);
    }

    public OutputCommentDto addCommentForOrder(InputCommentDto inputCommentDto){
        Comment savedComment = super.save(convertInputToEntity(inputCommentDto));
        return convertEntityToOutputDto(savedComment);
    }


    @Override
    public Comment convertInputToEntity(InputCommentDto inputCommentDto) {
        return Comment.builder()
                .opinion(inputCommentDto.getOpinion())
                .order(orderService.loadById(inputCommentDto.getOrderId()))
                .build();
    }

    @Override
    public OutputCommentDto convertEntityToOutputDto(Comment comment) {
         return OutputCommentDto.builder()
                 .id(comment.getId())
                 .orderId(comment.getOrder().getId())
                 .build();
    }
}
