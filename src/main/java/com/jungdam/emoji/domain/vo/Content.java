package com.jungdam.emoji.domain.vo;

import com.jungdam.error.dto.ErrorMessage;
import com.jungdam.error.exception.common.InvalidArgumentException;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.StringUtils;

@Embeddable
public class Content {

    @Column(name = "emoji_content")
    private String content;

    protected Content() {
    }

    public Content(String content) {
        validate(content);
        this.content = content;
    }

    private void validate(String content) {
        if (!StringUtils.hasText(content)) {
            throw new InvalidArgumentException(ErrorMessage.INVALID_DIARY_CONTENT);
        }
    }

    public String getContent() {
        return content;
    }
}