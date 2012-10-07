/*
 * Copyright 2012, Google Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jf.smalidea;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jf.smalidea.psi.ElementTypes;
import org.jf.smalidea.psi.impl.*;
import org.jf.smalidea.psi.stub.element.SmaliFileElementType;

public class SmaliParserDefinition implements ParserDefinition {
    @NotNull
    public Lexer createLexer(Project project) {
        return new SmaliLexer();
    }

    public PsiParser createParser(Project project) {
        return new SmaliParser();
    }

    public IFileElementType getFileNodeType() {
        return SmaliFileElementType.INSTANCE;
    }

    private static final TokenSet WHITESPACE = TokenSet.create(TokenType.WHITE_SPACE);
    @NotNull
    public TokenSet getWhitespaceTokens() {
        return WHITESPACE;
    }

    private static final TokenSet COMMENT = TokenSet.create(SmaliTokens.LINE_COMMENT);
    @NotNull
    public TokenSet getCommentTokens() {
        return COMMENT;
    }

    private static final TokenSet STRING_LITERAL = TokenSet.create(SmaliTokens.STRING_LITERAL);
    @NotNull
    public TokenSet getStringLiteralElements() {
        return STRING_LITERAL;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        if (node.getElementType() == ElementTypes.SMALI_CLASS) {
            return new SmaliClassImpl(node);
        } else if (node.getElementType() == ElementTypes.METHOD) {
            return new SmaliMethodImpl(node);
        } else if (node.getElementType() == ElementTypes.INSTRUCTION) {
            return new SmaliInstructionImpl(node);
        } else if (node.getElementType() == ElementTypes.LITERAL) {
            return new SmaliLiteralImpl(node);
        } else if (node.getElementType() == ElementTypes.REGISTERS_SPEC) {
            return new SmaliRegistersSpecImpl(node);
        } else if (node.getElementType() == ElementTypes.VOID_TYPE) {
            return new SmaliVoidTypeElementImpl(node);
        } else if (node.getElementType() == ElementTypes.PRIMITIVE_TYPE) {
            return new SmaliPrimitiveTypeElementImpl(node);
        }
        return new ASTWrapperPsiElement(node);
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new SmaliFileImpl(viewProvider);
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        //TODO: implement this
        return SpaceRequirements.MUST;
    }
}
