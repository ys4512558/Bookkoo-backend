import React, { useState, useEffect } from 'react';
import { useParams, useLocation } from 'react-router-dom';
import useModal from '@hooks/useModal';
import SettingsModal from '@components/@common/SettingsModal';
import { authAxiosInstance } from '../../services/axiosInstance';

const CurationLetterDetail = () => {
  const { id } = useParams();
  const location = useLocation();
  const { isOpen, closeModal, toggleModal } = useModal();
  const [nickName, setnickName] = useState('');
  const [letter, setLetter] = useState('');
  const modalVisible = location.state?.modalVisible ?? true; // 기본값은 true로 설정

  useEffect(() => {
    authAxiosInstance
      .get('/members/info')
      .then(res => {
        console.log('member info:', res.data.nickName);
        setnickName(res.data.nickName);
      })
      .catch(err => {
        console.log(err);
      });
  }, []);

  useEffect(() => {
    authAxiosInstance
      .get(`/curations/detail/${id}`)
      .then(res => {
        setLetter(res.data);
        console.log(res);
      })
      .catch(err => {
        console.log(err);
      });
  }, [id]);

  // 레터 보관
  const handleLetterStore = () => {
    authAxiosInstance
      .post(`/curations/store/${id}`, {})
      .then(res => {
        console.log('Letter stored successfully:', res);
      })
      .catch(err => {
        console.log('error:', err);
      });
  };

  // 레터 삭제
  const handleLetterDelete = () => {
    // 연동
    authAxiosInstance
      .delete(`/curations/${id}`, {})
      .then(responses => {
        console.log('Letters deleted successfully:', responses);
      })
      .catch(err => {
        console.log('Error deleting letters:', err);
      });
  };

  const actions = [
    { label: '레터 보관', onClick: handleLetterStore },
    { label: '레터 삭제', onClick: handleLetterDelete },
  ];

  return (
    <div className='flex flex-col items-center justify-start p-4  scrollbar-none'>
      <div className='relative bg-white rounded-lg shadow-lg w-full max-w-md mx-auto mt-32 scrollbar-none'>
        <div className='absolute -top-28 w-full flex justify-center z-20'>
          <img
            src={letter.coverImgUrl}
            alt={letter.title}
            className='w-48 h-64 rounded-md shadow-lg z-10'
          />
        </div>
        {/* 설정 모달 */}
        <div className='relative flex flex-col items-center p-6 pt-32 z-30'>
          {/* <SettingsModal
            isOpen={isOpen}
            onClose={closeModal}
            onToggle={toggleModal}
            actions={actions}
          /> */}
          {nickName !== letter.writer && modalVisible && (
            <SettingsModal
              isOpen={isOpen}
              onClose={closeModal}
              onToggle={toggleModal}
              actions={actions}
            />
          )}
        </div>
        <div className='min-h-44 px-6 py-8 text-center scrollbar-none'>
          <h2 className='text-xl font-bold mb-2'>{letter.title}</h2>
          <div className='text-gray-600 mb-4 scrollbar-none'>
            {letter.content}
          </div>
        </div>
        <div className='bg-green-400 px-6 py-3 rounded-b-lg flex justify-between text-sm text-gray-700'>
          <span>{new Date(letter.createdAt).toLocaleDateString()}</span>
          <span>FROM: {letter.writer}</span>
        </div>
      </div>
    </div>
  );
};

export default CurationLetterDetail;
