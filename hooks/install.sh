#!/bin/sh
if test -d hooks
then
  cd hooks
fi
ts=`date +%s`
for hook in *
do
  case $hook in
    install*)
      ;;
    *)
      dst=../.git/hooks/$hook
      mv $dst $dst.old-$ts 2>/dev/null
      cp $hook $dst
      chmod a+x $dst
      ;;
  esac
done
