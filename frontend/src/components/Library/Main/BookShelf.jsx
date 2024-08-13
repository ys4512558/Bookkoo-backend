import React from 'react';
import Book from './Book';
import EmptySlot from './EmptySlot';

const getSecondaryColorClass = primaryColorClass => {
  const colorMap = {
    200: '400',
    400: '200',
    600: '200',
  };

  const primaryClassSuffix = primaryColorClass.match(/-(\d{3})$/)?.[1];
  const secondaryClassSuffix = colorMap[primaryClassSuffix] || '600';

  return primaryColorClass.replace(primaryClassSuffix, secondaryClassSuffix);
};

const BookShelf = ({
  books,
  moveBook,
  onBookClick,
  viewOnly,
  libraryStyleDto,
}) => {
  const totalSlots = 21; // 3층에 7개의 슬롯

  let primaryColorClass = libraryStyleDto?.libraryColor;

  if (!primaryColorClass || primaryColorClass === '#FFFFFF') {
    primaryColorClass = 'bg-[#a27045]';
  }

  const secondaryColorClass =
    primaryColorClass === 'bg-[#a27045]'
      ? 'bg-[#d2a679]'
      : getSecondaryColorClass(primaryColorClass);

  console.log('Primary Color Class:', primaryColorClass);
  console.log('Secondary Color Class:', secondaryColorClass);

  const allSlots = Array.from({ length: totalSlots }, (_, index) => {
    const book = books.find(book => book.bookOrder === index + 1);
    return book && book.book ? (
      <Book
        key={book.book.id}
        item={book}
        index={index}
        moveBook={moveBook}
        onBookClick={onBookClick}
        viewOnly={viewOnly}
        libraryStyleDto={libraryStyleDto}
      />
    ) : (
      <EmptySlot
        key={`empty-${index}`}
        index={index}
        moveBook={moveBook}
        viewOnly={viewOnly}
      />
    );
  });

  const renderShelf = (start, end, colorClass) => (
    <div className='flex justify-center mb-2'>
      <div
        className={`flex flex-nowrap px-1 justify-center w-full rounded-xl shadow-lg ${colorClass}`}
      >
        {allSlots.slice(start, end)}
      </div>
    </div>
  );

  return (
    <div className='p-4 flex flex-col items-center'>
      <div
        className={`rounded-xl shadow-lg w-full max-w-full overflow-x-auto p-2 ${secondaryColorClass}`}
      >
        {books.length > 0 ? (
          <>
            {renderShelf(0, 7, primaryColorClass)} {/* 1층 */}
            {renderShelf(7, 14, primaryColorClass)} {/* 2층 */}
            {renderShelf(14, 21, primaryColorClass)} {/* 3층 */}
          </>
        ) : (
          <p className='text-center text-gray-500'>서재에 책이 없습니다.</p>
        )}
      </div>
    </div>
  );
};

export default BookShelf;
