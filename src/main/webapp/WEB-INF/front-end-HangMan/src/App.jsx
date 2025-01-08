import Navbar from './Components/Navbar/Navbar';
import Home from './pages/Home/Home';
import Footer from './Components/Footer/Footer';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './pages/Login/Login';
import SinglePlayerGame from './pages/Game/Single-PlayerGame';
import MultiPlayerGame from './pages/Game/MultiPlayerGame';
import LoginGiver from './pages/Login/LoginGiver';
import LoginGuesser from './pages/Login/LoginGuesser';
import WordToGuess from './pages/WordToGuess/WordsToGuess';
import History from './pages/Statistics/History';
import MostUsedWords from './pages/Statistics/MostUsedWords';
import Ranking from './pages/Statistics/Ranking';
import TopTenRankings from './pages/Statistics/TopTenRanking';
import Error from './pages/Errors/Error';
import Register from './pages/Register/Register';
import Welcome from './pages/Home/Welcome';
import JoinCreateGame from './pages/WordToGuess/JoinCreateGame';
import JoinGame from './pages/WordToGuess/JoinGame';
import MultiPlayerCode from './pages/Game/MultiPlayerCode';
import Admin from './pages/Admin/Admin';
import { useEffect, useState } from "react";
import ProtectedRoutes from './utils/ProtectedRoutes';
import { AuthProvider } from './utils/AuthContext';

function App() {
  const [roles, setRoles] = useState(sessionStorage.getItem('role')?.split(',') || []);

  return (
    <Router>
      <AuthProvider>
        <div className="App">
          <Navbar />
          <div className="content">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/welcome" element={<Welcome />} />
              <Route path="/login" element={<Login />} />
              <Route path='/register' element={<Register />} />
              <Route path='/statistics' element={<MostUsedWords />} />
              <Route path='/rankings' element={<Ranking />} />
              <Route path='/rankings/top-ten' element={<TopTenRankings />} />
              <Route path='/error' element={<Error />} />
              <Route path='/history' element={<ProtectedRoutes><History /></ProtectedRoutes>} />
              <Route path='/single-player/games' element={<ProtectedRoutes><SinglePlayerGame /></ProtectedRoutes>} />
              <Route path="/multi-player/guesser" element={<ProtectedRoutes><LoginGuesser /></ProtectedRoutes>} />
              <Route path="/multi-player/giver" element={<ProtectedRoutes><LoginGiver /></ProtectedRoutes>} />
              <Route path='/multi-player/words' element={<ProtectedRoutes><WordToGuess /></ProtectedRoutes>} />
              <Route path='/multi-player/games' element={<ProtectedRoutes><MultiPlayerGame /></ProtectedRoutes>} />
              <Route path='/multi-player/code' element={<ProtectedRoutes><MultiPlayerCode /></ProtectedRoutes>} />
              <Route path='/multi-player' element={<ProtectedRoutes><JoinCreateGame /></ProtectedRoutes>} />
              <Route path='/multi-player/join' element={<ProtectedRoutes><JoinGame /></ProtectedRoutes>} />
              {roles.includes('ADMIN') &&
                (<Route path='/admin' element={<Admin />} />)
              }
            </Routes>
          </div>
          <Footer />
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
