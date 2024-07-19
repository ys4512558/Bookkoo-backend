import React from 'react';
import { createBrowserRouter } from 'react-router-dom';
import App from './App.jsx';
import LibraryHome from './pages/LibraryHome.jsx';
import LibraryDetail from './pages/LibraryDetail.jsx';
import LibrarySearch from './pages/LibrarySearch.jsx';
import LibrarySearchDetail from './pages/LibrarySearchDetail.jsx';
import LibraryMain from './pages/LibraryMain.jsx';
import Register from './pages/Register.jsx';
import CurationReceive from './pages/CurationReceive.jsx';
import CurationSend from './pages/CurationSend.jsx';
import CurationStore from './pages/CurationStore.jsx';

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        path: '/',
        element: <LibraryHome />,
      },
      {
        path: '/detail/:id',
        element: <LibraryDetail />,
      },
      {
        path: 'library',
        element: <LibraryMain />,
      },
      {
        path: 'register',
        element: <Register />,
      },
      {
        path: 'library/search',
        element: <LibrarySearch />,
      },
      {
        path: 'library/search/:bookId',
        element: <LibrarySearchDetail />,
      },
      {
        path: 'curation/receive',
        element:<CurationReceive/>,
      },
      {
        path: 'curation/send',
        element:<CurationSend/>,
      },
      {
        path: 'curation/store',
        element:<CurationStore/>,
      },
    ],
  },
]);

export default router;
