import styles from './Video.module.scss'

const VideoBackground = (props) => {
  return (
    <div className={styles['video-background']}>
      <video preload='auto' autoPlay muted loop>
        <source src='/videoLower.mp4' type="video/mp4" />
      </video>
      {props.children}
    </div>
  );
};

export default VideoBackground;