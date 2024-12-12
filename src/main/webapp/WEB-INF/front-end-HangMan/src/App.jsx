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
import HistoryLogIn from './pages/Login/HistoryLogIn';
import MostUsedWords from './pages/Statistics/MostUsedWords';
import Ranking from './pages/Statistics/Ranking';
import TopTenRankings from './pages/Statistics/TopTenRanking';
import Error from './pages/Errors/Error';
import Register from './pages/Register/Register';

function App() {

  return (
    <Router>
      <div className="App">
        <Navbar />
        <div className="content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path='/single-player/games' element={<SinglePlayerGame />} />
            <Route path="/multi-player/guesser" element={<LoginGuesser />} />
            <Route path="/multi-player/giver" element={<LoginGiver />} />
            <Route path='/multi-player/words' element={<WordToGuess />} />
            <Route path='/multi-player/games' element={<MultiPlayerGame />} />
            <Route path='/history/log-in' element={<HistoryLogIn />} />
            <Route path='/history' element={<History />} />
            <Route path='/statistics' element={<MostUsedWords />} />
            <Route path='/rankings' element={<Ranking />} />
            <Route path='/rankings/top-ten' element={<TopTenRankings />} />
            <Route path='/error' element={<Error />} />
            {/* <Route path='/logout' element={<Logout />} /> */}
            {/* <Route path='/admin' element={<Admin />} /> */}
            <Route path='/register' element={<Register />} />
          </Routes>
        </div>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
