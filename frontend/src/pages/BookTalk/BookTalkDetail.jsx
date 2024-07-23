import React, { useState } from 'react';
import ChatBubble from '@components/@common/ChatBubble';
import Button from '@components/@common/Button';
import Input from '@components/@common/Input';
import { comments, talkbook } from '@mocks/BookTalkData';

const BookTalkDetail = () => {
  const [inputMessage, setInputMessage] = useState('');
  const [book, setBook] = useState(talkbook);
  const [commentList, setCommentList] = useState(comments);

  const handleInputChange = e => {
    const message = e.target.value;
    if (message.length <= 1000) {
      setInputMessage(message);
    }
  };

  return (
    <div className='flex flex-col min-h-[calc(100vh-121px)]'>
      <div className='flex flex-col items-center'>
        <div className='w-32 min-h-40 my-4 flex items-center '>
          <img
            src={book.cover_img_url}
            alt='Book Cover'
            className='rounded-lg'
          />
        </div>

        <div className='text-center mb-3'>
          <h2 className='text-xl font-semibold'>{book.title}</h2>
          <p className='text-gray-500'>{book.author}</p>
        </div>
        {/* <h3 className='text-lg font-semibold my-4'>{book.content}</h3> */}
      </div>
      <div className='flex-1 overflow-y-auto p-4 scrollbar-none'>
        <div className='space-y-4'>
          {commentList.map((comment, index) => (
            <ChatBubble
              key={index}
              message={comment.message}
              role='other'
              showProfile={true}
              showLikes={true}
              likes={comment.likes}
              profileImage={comment.profileImage}
            />
          ))}
        </div>
      </div>
      <div className='bg-white p-4 sticky bottom-0'>
        <div className='max-w-md w-full flex flex-row items-center justify-center mx-auto'>
          <div className='flex-grow'>
            <Input
              type='text'
              value={inputMessage}
              onChange={handleInputChange}
              placeholder='내용을 입력해주세요(1000자 이내)'
              customClass='border rounded-l-lg focus:outline-none'
            />
          </div>
          <Button type='submit' className='ml-2'>
            등록
          </Button>
        </div>
      </div>
    </div>
  );
};

export default BookTalkDetail;
