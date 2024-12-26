#!/bin/sh
if test -d hooks
then
  cd hooks
fi
case $1 in
  -c|--clobber)
    ;;
  '')
    ts=`date +%s`
    ;;
  *)
    echo Unrecognized argument $1. >2
    exit 1
    ;;
esac
for hook in *
do
  case $hook in
    install*)
      ;;
    *)
      dst=../.git/hooks/$hook
      if test $ts
      then
        mv $dst $dst.old-$ts 2>/dev/null
      fi
      cp $hook $dst
      chmod a+x $dst
      ;;
  esac
done
